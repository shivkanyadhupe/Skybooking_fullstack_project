package com.example.demo.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Collections;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ReactiveJwtDecoder reactiveJwtDecoder) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> exchanges

                // Allow Swagger UI
                .pathMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()

                // Public access
                .pathMatchers("/api/users/register", "/api/users/login").permitAll()
                 
                //validate Token
                //.pathMatchers(HttpMethod.GET,"/api/auth/validate").permitAll()
                // User management
                .pathMatchers(HttpMethod.GET, "/api/users/{id}").hasRole("ADMIN")

                // Flight search
                .pathMatchers(HttpMethod.GET, "/flights/search").permitAll()
                .pathMatchers(HttpMethod.GET, "/flights").permitAll()
                .pathMatchers(HttpMethod.POST, "/flights").hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT, "/flights/{id}").hasRole("ADMIN")
                .pathMatchers(HttpMethod.GET, "/flights/{id}").hasRole("ADMIN")
                .pathMatchers(HttpMethod.DELETE, "/flights/{id}").hasRole("ADMIN")

                // Fare
                .pathMatchers(HttpMethod.GET, "/fare").permitAll()
                .pathMatchers(HttpMethod.GET, "/fare/{id}").permitAll()
                .pathMatchers(HttpMethod.GET, "/fare/all").hasRole("ADMIN")
                .pathMatchers(HttpMethod.POST, "/fare/create").hasRole("ADMIN")

                // Booking
                .pathMatchers(HttpMethod.POST, "/booking/create").hasRole("USER")
                .pathMatchers(HttpMethod.GET, "/booking/all").hasRole("ADMIN")
                .pathMatchers(HttpMethod.GET, "/booking/{id}").permitAll()
                .pathMatchers(HttpMethod.PUT, "/booking/{id}").hasRole("USER")
                .pathMatchers(HttpMethod.DELETE,"/booking/{id}").hasRole("ADMIN")

                // Default: all other requests need authentication
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                        .jwtDecoder(reactiveJwtDecoder)
                        .jwtAuthenticationConverter(grantedAuthoritiesExtractor())
                )
            );
        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        SecretKey key = getSigningKey();
        return NimbusReactiveJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            String role = jwt.getClaimAsString("role");
            if (role == null) {
                return Collections.emptyList();
            }

            // 🔧 Fix: ensure role has "ROLE_" prefix once
            String finalRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
            return Collections.singletonList(new SimpleGrantedAuthority(finalRole));
        });
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = this.jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
