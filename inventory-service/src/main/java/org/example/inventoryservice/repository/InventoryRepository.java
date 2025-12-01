package org.example.inventoryservice.repository;

import org.example.inventoryservice.model.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<StockItem, String> {
    /**
     * Finds a StockItem entity by its SKU code
     * */
    StockItem findBySkuCode(String skuCode);
}
