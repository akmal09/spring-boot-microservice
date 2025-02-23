package com.projects.microservice.product;

import com.projects.microservice.product.dto.ProductRequest;
import com.projects.microservice.product.dto.ProductResponse;
import com.projects.microservice.product.model.Product;
import com.projects.microservice.product.repository.ProductRepository;
import com.projects.microservice.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Autowired(required = true)
public class ProductServiceApplicationTests {
    @Autowired
    private MockMvc mockMvc;

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

//        ProductResponse productResponse = productService.createProduct(productRequest);
//        System.out.println(productResponse);
//        assertTrue(productResponse.isCreated());
    }

    @Test
    void healthCheck()throws Exception{
        mockMvc.perform(post("/api/product/health"))
                .andExpect(status().isOk());

    }

    @Test
    void getAllProductsTest() {
//        when(productRepository.findAll()).thenReturn();
        ResponseEntity<?> list =  productService.getAllProducts();
        assertEquals(HttpStatus.OK,list.getStatusCode());
    }
}
