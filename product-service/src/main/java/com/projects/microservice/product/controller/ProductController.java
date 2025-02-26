package com.projects.microservice.product.controller;

import com.projects.microservice.product.dto.ProductRequest;
import com.projects.microservice.product.dto.ProductResponse;
import com.projects.microservice.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CacheManager cacheManager;

    @Operation(summary = "Create a Product", description = "Create product with json request body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully add product"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    @Operation(summary = "Get all products", description = "Get list of product that has been saved")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful created"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id){
        return productService.getProductByIdCache(id);
    }
    @PutMapping("/{id}/{newName}")
    public ResponseEntity<?> update(@PathVariable Long id, @PathVariable String newName){
        return productService.updateProduct(id, newName);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return productService.deleteProduct(id);
    }

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck(){
        return new ResponseEntity<>("health check ok",HttpStatus.OK);
    }
    @GetMapping("/cek-cache")
    public ResponseEntity<?> cache(){
        cacheManager.getCache("productsCache");
        return new ResponseEntity<>("health check ok",HttpStatus.OK);
    }
}
