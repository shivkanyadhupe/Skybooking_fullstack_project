package com.ecommerce.user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.user_service.client.ProductClient;

@RestController
@RequestMapping("/users")
public class UserController {
	 @Autowired
	    private ProductClient productClient;

	    @GetMapping
	    public String getUsersWithProducts() {
	        return "Users → " + productClient.getProducts();
	    }

}
