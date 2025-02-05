package com.projects.microservices.order.service;

import com.projects.microservices.order.dto.OrderRequest;
import com.projects.microservices.order.model.Order;
import com.projects.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final String inventoryServiceUrl = "http://inventory-service";


    public ResponseEntity<?> placeOrder(OrderRequest orderRequest) {
        try{
            String checkStock = inventoryServiceUrl+"/api/inventory/is-in-stock/" + orderRequest.getSkuCode() + "/" + orderRequest.getQuantity();
            Boolean productExists = restTemplate.getForObject(checkStock, Boolean.class);

            if (Boolean.FALSE.equals(productExists)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("sku does not exist");
            }

            String calculateStock = inventoryServiceUrl+"/api/inventory/calculate-stock/"+ orderRequest.getSkuCode() + "/" + orderRequest.getQuantity();


            Boolean res = restTemplate.postForObject(calculateStock, orderRequest, Boolean.class);

            if(Objects.isNull(res)) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("inventory Service Error");

            if(Boolean.FALSE.equals(res)) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("inventory Service Error");

            Order order = new Order();
            order.setOrderNumber(orderRequest.getOrderNumber());
            order.setSkuCode(orderRequest.getSkuCode());
            order.setPrice(orderRequest.getPrice());
            order.setQuantity(orderRequest.getQuantity());
            orderRepository.save(order);

            return ResponseEntity.ok("Order placed successfully");
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}
