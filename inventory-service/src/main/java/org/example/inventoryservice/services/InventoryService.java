package org.example.inventoryservice.services;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.dto.ProductInfo;
import org.example.domain.dto.events.ProductAddedEvent;
import org.example.inventoryservice.client.ProductClient;
import org.example.inventoryservice.dto.StockItemRequest;
import org.example.inventoryservice.dto.StockItemResponse;
import org.example.inventoryservice.error.ItemNotFoundException;
import org.example.inventoryservice.error.OutOfStockException;
import org.example.inventoryservice.model.StockItem;
import org.example.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final ProductClient productClient;
    private final InventoryRepository inventoryRepository;


    public void addOrUpdateInventoryItem(StockItemRequest stockItemRequest){
        StockItem stockItem = StockItem.builder()
                .skuCode(stockItemRequest.getSkuCode())
                .quantity(stockItemRequest.getQuantity())
                .build();
        inventoryRepository.save(stockItem);
    }


    public StockItemResponse getItemBySkuCode(String skuCode) {
        StockItem verifyItem = verifyItemExists(skuCode);

        return  StockItemResponse.builder()
                .skuCode(verifyItem.getSkuCode())
                .quantity(verifyItem.getQuantity())
                .build();
    }

    //API context (for controller)
    public int reduceInventoryItem(String skuCode) {
        StockItem verifiedItem = verifyItemExists(skuCode);

        if(verifiedItem.getQuantity() <= 0) throw new OutOfStockException("Item " + skuCode + " is out of stock");

        verifiedItem.setQuantity(verifiedItem.getQuantity() - 1);

        StockItem savedItem = inventoryRepository.save(verifiedItem);

        return savedItem.getQuantity();
    }

    //Event context (for EventListener

    public void handleInventoryEvent(ProductAddedEvent event){

        switch(event.getAction()){
            case CREATE -> handleCreateInventoryEvent(event.getSkuCode(), event.getQuantity());
            case ADD -> handleAddInventoryEvent(event.getSkuCode(), event.getQuantity());
            case REDUCE -> handleReduceInventoryEvent(event.getSkuCode(), event.getQuantity());
            case DELETE -> handleDeleteInventoryEvent(event.getSkuCode());
            default -> throw new IllegalArgumentException("Unknown action: " + event.getAction());
        }
    }

    private void handleDeleteInventoryEvent(String skuCode) {
        StockItem verifiedItem = verifyItemExists(skuCode);

        inventoryRepository.delete(verifiedItem);
        log.info("item {} has been deleted", verifiedItem);
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
        StockItem verifiedItem = verifyItemExists(skuCode);

        if(verifiedItem.getQuantity() <= 0) throw new OutOfStockException("Item " + skuCode + " is out of stock");

        verifiedItem.setQuantity(verifiedItem.getQuantity() - quantity);

        StockItem savedItem = inventoryRepository.save(verifiedItem);

        log.info("Item {} has been reduced to {}", savedItem.getSkuCode(), quantity);
    }

    public void handleAddInventoryEvent(String skuCode, int quantity) {
        StockItem verifiedItem = verifyItemExists(skuCode);

        if(verifiedItem.getQuantity() <= 0) throw new OutOfStockException("Item " + skuCode + " is out of stock");

        verifiedItem.setQuantity(verifiedItem.getQuantity() + quantity);

        StockItem savedItem = inventoryRepository.save(verifiedItem);

        log.info("Item {} has been added to {}", savedItem.getSkuCode(), quantity);
    }

    private StockItem verifyItemExists(String skuCode) {
    return Optional.ofNullable(inventoryRepository.findBySkuCode(skuCode))
            .orElseThrow(() -> new ItemNotFoundException(skuCode));
    }

    public List<ProductInfo> getItemsBySkuCode(List<String> skuCodes) {

        List<ProductInfo> items;
        try{
             items = productClient.getItemsBySkuCodes(skuCodes);
        }catch (FeignException.NotFound e){
            log.error("items not found");
            throw new ItemNotFoundException("items not found");
        }catch (FeignException e){
            log.error("Trouble communicating with the product service");
            throw new ItemNotFoundException("items not found");
        }

        for (ProductInfo productInfo : items){
            StockItem item =  inventoryRepository.findBySkuCode(productInfo.getSkuCode());
            productInfo.setQuantity(item.getQuantity());
        }
        return items;
    }
}
