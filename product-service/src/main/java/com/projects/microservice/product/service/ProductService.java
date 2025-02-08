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

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        try{
            Product product = Product.builder()
                    .name(productRequest.name())
                    .description(productRequest.description())
                    .skuCode(productRequest.skuCode())
                    .price(productRequest.price())
                    .build();
            Product saveProduct = productRepository.save(product);
            log.info("Product created successfully");
            return new ProductResponse(product.getId(), product.getName(), product.getDescription(),
                    product.getSkuCode(),
                    product.getPrice(),
                    Objects.isNull(saveProduct)?false:true);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ProductResponse(null,null,null,null,null,false);
        }
    }

    public ResponseEntity<?> getAllProducts() {
        List<ProductResponse> lists = productRepository.findAll()
                .stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getDescription(),
                        product.getSkuCode(),
                        product.getPrice(),
                        true))
                .toList();
        return new ResponseEntity<>(lists, HttpStatus.OK);
    }
}
