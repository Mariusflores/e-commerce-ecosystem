package org.example.inventoryservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.inventoryservice.InventoryRepository;
import org.example.inventoryservice.dto.InventoryEvent;
import org.example.inventoryservice.dto.StockItemRequest;
import org.example.inventoryservice.dto.StockItemResponse;
import org.example.inventoryservice.error.OutOfStockException;
import org.example.inventoryservice.model.StockItem;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;


    public void addOrUpdateInventoryItem(StockItemRequest stockItemRequest){
        StockItem stockItem = StockItem.builder()
                .skuCode(stockItemRequest.getSkuCode())
                .quantity(stockItemRequest.getQuantity())
                .build();
        inventoryRepository.save(stockItem);
    }


    public StockItemResponse getItemBySkuCode(String skuCode) {
        StockItem item = inventoryRepository.findBySkuCode(skuCode);

        return  StockItemResponse.builder()
                .skuCode(item.getSkuCode())
                .quantity(item.getQuantity())
                .build();
    }

    //API context (for controller)
    public int reduceInventoryItem(String skuCode) {
        StockItem item = inventoryRepository.findBySkuCode(skuCode);

        if(item.getQuantity() <= 0) throw new OutOfStockException("Item " + skuCode + " is out of stock");

        item.setQuantity(item.getQuantity() - 1);

        StockItem savedItem = inventoryRepository.save(item);

        return savedItem.getQuantity();
    }

    //Event context (for EventListener

    public void handleInventoryEvent(InventoryEvent event){

        switch(event.getAction()){
            case CREATE -> handleCreateInventoryEvent(event.getSkuCode(), event.getQuantity());
            case ADD -> handleAddInventoryEvent(event.getSkuCode(), event.getQuantity());
            case REDUCE -> handleReduceInventoryEvent(event.getSkuCode(), event.getQuantity());
            default -> throw new IllegalArgumentException("Unknown action: " + event.getAction());
        }
    }

    private void handleCreateInventoryEvent(String skuCode, int quantity) {

        StockItem item = StockItem.builder()
                .skuCode(skuCode)
                .quantity(quantity)
                .build();
        inventoryRepository.save(item);
        log.info("Created Inventory Event for skuCode: {}, quantity: {}", skuCode, quantity);

    }

    public void handleReduceInventoryEvent(String skuCode, int quantity) {
        StockItem item = inventoryRepository.findBySkuCode(skuCode);

        if(item.getQuantity() <= 0) throw new OutOfStockException("Item " + skuCode + " is out of stock");

        item.setQuantity(item.getQuantity() - quantity);

        StockItem savedItem = inventoryRepository.save(item);

        log.info("Item {} has been reduced to {}", savedItem.getSkuCode(), quantity);
    }

    public void handleAddInventoryEvent(String skuCode, int quantity) {
        StockItem item = inventoryRepository.findBySkuCode(skuCode);

        if(item.getQuantity() <= 0) throw new OutOfStockException("Item " + skuCode + " is out of stock");

        item.setQuantity(item.getQuantity() + quantity);

        StockItem savedItem = inventoryRepository.save(item);

        log.info("Item {} has been added to {}", savedItem.getSkuCode(), quantity);
    }
}
