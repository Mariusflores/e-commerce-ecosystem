package org.example.inventoryservice;

import org.example.inventoryservice.model.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<StockItem, String> {

}
