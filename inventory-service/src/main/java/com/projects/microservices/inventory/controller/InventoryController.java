package com.projects.microservices.inventory.controller;

import com.projects.microservices.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @Operation(summary = "Check for Stock", description = "checking stocks of product based on skucode")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "true", description = "If product is in stock"),
            @ApiResponse(responseCode = "false", description = "if product is not in stock")
    })
    @GetMapping("/is-in-stock/{skuCode}/{quantity}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable String skuCode, @PathVariable Integer quantity) {
        return inventoryService.isInStock(skuCode, quantity);
    }

    @Operation(summary = "calculate stock of products", description = "calculate stock after order is received")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "true", description = "calculation is success"),
            @ApiResponse(responseCode = "false", description = "calculation is failed")
    })
    @PostMapping("/calculate-stock/{skuCode}/{quantity}")
    public boolean calculateInventory(@PathVariable String skuCode, @PathVariable Integer quantity) {
        return inventoryService.calculateStock(skuCode,quantity);
    }


}
