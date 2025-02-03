package com.projects.microservices.order.service;

import com.projects.microservices.order.client.InventoryClient;
import com.projects.microservices.order.dto.OrderRequest;
import com.projects.microservices.order.model.Order;
import com.projects.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;


    public void placeOrder(OrderRequest orderRequest) {
        var isProductInStock = inventoryClient.isInStock(orderRequest.getSkuCode(), orderRequest.getQuantity());
        if (isProductInStock) {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.getPrice().multiply(BigDecimal.valueOf(orderRequest.getQuantity())));
            order.setSkuCode(orderRequest.getSkuCode());
            order.setQuantity(orderRequest.getQuantity());
            orderRepository.save(order);
        }else{
            throw new RuntimeException("Product with SKU Code "+orderRequest.getSkuCode()+"is not available");
        }
    }
}
