package org.example.inventoryservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.inventoryservice.dto.StockItemRequest;
import org.example.inventoryservice.dto.StockItemResponse;
import org.example.inventoryservice.services.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public StockItemResponse getItemBySkuCode(@PathVariable String skuCode){
        return inventoryService.getItemBySkuCode(skuCode);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addOrUpdateInventoryItem(@RequestBody StockItemRequest request){
        inventoryService.addOrUpdateInventoryItem(request);
    }

    @PutMapping("/{skuCode}/reduce")
    @ResponseStatus(HttpStatus.OK)
    public int reduceInventoryItem(@PathVariable String skuCode){
        return inventoryService.reduceInventoryItem(skuCode);
    }
}
