package com.projects.microservices.order.controller;

import com.projects.microservices.order.dto.OrderRequest;
import com.projects.microservices.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final RestTemplate restTemplate;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest orderRequest) {

        String url = "http://inventory-service/api/inventory/is-in-stock/" + orderRequest.getSkuCode()+"/"+orderRequest.getQuantity();
        Boolean productExists = restTemplate.getForObject(url, Boolean.class);

        if (Boolean.FALSE.equals(productExists)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("sku does not exist");
        }

        return ResponseEntity.ok("Order placed successfully");
    }



    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please order after some time!");
    }
}
