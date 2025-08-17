package com.example.demo.filter;

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest; 
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component 
public class AddUserInfoHeaderFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(AddUserInfoHeaderFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(context -> context.getAuthentication() != null && context.getAuthentication() instanceof JwtAuthenticationToken)
                // Map the context to the JwtAuthenticationToken
                .map(context -> (JwtAuthenticationToken) context.getAuthentication())
                // If a valid JWT token is found, process it
                .flatMap(authentication -> {
                    Jwt jwt = authentication.getToken(); 
                    ServerHttpRequest request = exchange.getRequest();

                    String userId = jwt.getClaimAsString("userId");
                    String email = jwt.getSubject(); 
                    String role = jwt.getClaimAsString("role"); 
                    log.debug("Adding user headers: X-User-ID={}, X-User-Email={}, X-User-Roles={}", userId, email, role);

                    ServerHttpRequest mutatedRequest = request.mutate()
                            .header("X-User-ID", userId != null ? userId : "") 
                            .header("X-User-Email", email != null ? email : "") 
                            .header("X-User-Roles", role != null ? role : "")   
                            .build();

                    ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

                    return chain.filter(mutatedExchange);
                })
                
                .switchIfEmpty(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        
        return 1;
    }
}