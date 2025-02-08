package com.projects.microservice.product.dto;

import java.math.BigDecimal;

public record ProductResponse(String id, String name, String description,
                              String skuCode, BigDecimal price, Boolean isCreated) {
}
