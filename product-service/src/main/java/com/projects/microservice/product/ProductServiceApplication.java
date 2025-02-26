package com.projects.microservice.product;

import com.projects.microservices.cache.CacheConfig;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.annotation.Import;

@OpenAPIDefinition(
        info = @Info(
                title = "Product Service API",
                version = "1.0",
                description = "API for managing products"
        )
)
@SpringBootApplication
@Import(CacheConfig.class)
public class ProductServiceApplication {
    public static void main(String[] args) {
        Test123 test123 = new Test123();
        System.out.println(test123.test());
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}