package com.projects.microservice.product.service;

import com.projects.microservice.product.dto.DbDummyResponse;
import com.projects.microservice.product.dto.ProductRequest;
import com.projects.microservice.product.dto.ProductResponse;
import com.projects.microservice.product.model.Product;
import com.projects.microservice.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
//@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    private final Map<Long, String> databaseSimulator = new HashMap<>();

    public ProductService(ProductRepository productRepository, ProductRepository productRepository1) {
        this.productRepository = productRepository1;
        databaseSimulator.put(1L, "Laptop");
        databaseSimulator.put(2L, "Smartphone");
        databaseSimulator.put(3L, "Tablet");
    }

    public ResponseEntity<ProductResponse> createProduct(ProductRequest productRequest) {
        try{
            Product product = Product.builder()
                    .name(productRequest.name())
                    .description(productRequest.description())
                    .skuCode(productRequest.skuCode())
                    .price(productRequest.price())
                    .build();
            Product saveProduct = productRepository.save(product);
            log.info("Product created successfully");
//            ProductResponse productResponse = ;
            return new ResponseEntity<>(
                    new ProductResponse(product.getId(), product.getName(), product.getDescription(),
                            product.getSkuCode(),
                            product.getPrice(),
                            Objects.isNull(saveProduct)?false:true),
                    HttpStatus.CREATED
            );
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(
                    new ProductResponse(null,null,null,null,null,false),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public ResponseEntity<?> getAllProducts() {
        try {
            List<ProductResponse> lists = productRepository.findAll()
                    .stream()
                    .map(product -> new ProductResponse(product.getId(), product.getName(), product.getDescription(),
                            product.getSkuCode(),
                            product.getPrice(),
                            true))
                    .toList();
            return new ResponseEntity<>(lists, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(
                    Collections.EMPTY_LIST,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

//    Dengan adanya cache, response akan disimpan ke dalam memory cache sehingga tidak perlu query lagi ke database.
    @Cacheable(value = "productsCache", key = "#id")
    public ResponseEntity<DbDummyResponse> getProductByIdCache(Long id) {
        // Simulate a slow database call
        try {
            Thread.sleep(1000); // Simulate delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DbDummyResponse dbDummyResponse = new DbDummyResponse(
                id,databaseSimulator.getOrDefault(id,"No context")
        );
        return new ResponseEntity<>(dbDummyResponse, HttpStatus.OK);
    }

    @CacheEvict(value = "productsCache", key = "#id")
    public ResponseEntity<?> updateProduct(Long id, String newName) {
        System.out.println("Updating product in database...");
        databaseSimulator.put(id, newName);
        return new ResponseEntity<>("update success", HttpStatus.OK);
    }

    @CacheEvict(value = "productsCache", key = "#id")
    public ResponseEntity<?> deleteProduct(Long id) {
        System.out.println("Deleting product from database...");
        databaseSimulator.remove(id);
        return new ResponseEntity<>("delete success", HttpStatus.OK);
    }
}
