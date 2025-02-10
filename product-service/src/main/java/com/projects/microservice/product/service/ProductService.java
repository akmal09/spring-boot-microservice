package com.projects.microservice.product.service;

import com.projects.microservice.product.dto.ProductRequest;
import com.projects.microservice.product.dto.ProductResponse;
import com.projects.microservice.product.model.Product;
import com.projects.microservice.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

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
}
