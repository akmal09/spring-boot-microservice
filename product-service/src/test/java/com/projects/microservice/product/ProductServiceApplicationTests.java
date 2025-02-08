package com.projects.microservice.product;

import com.projects.microservice.product.dto.ProductRequest;
import com.projects.microservice.product.dto.ProductResponse;
import com.projects.microservice.product.model.Product;
import com.projects.microservice.product.repository.ProductRepository;
import com.projects.microservice.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest

public class ProductServiceApplicationTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;
    @Test
    void createProductTest() {
        ProductRequest productRequest = new ProductRequest(
                "adsf2130",
                "test-product",
                "test description",
                "lala2134",
                BigDecimal.valueOf(123234)
                );

        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .skuCode(productRequest.skuCode())
                .price(productRequest.price())
                .build();

        when(productRepository.save(product)).thenReturn(product);

        ProductResponse productResponse = productService.createProduct(productRequest);
        System.out.println(productResponse);
        assertTrue(productResponse.isCreated());
    }

    @Test
    void getAllProductsTest() {
//        when(productRepository.findAll()).thenReturn();
        ResponseEntity<?> list =  productService.getAllProducts();
        assertEquals(HttpStatus.OK,list.getStatusCode());
    }
}
