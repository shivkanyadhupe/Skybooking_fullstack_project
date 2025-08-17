package com.ecommerce.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "product-service") // refers to Eureka registered name
public interface ProductClient {
    
    @GetMapping("/products")
    String getProducts();
}