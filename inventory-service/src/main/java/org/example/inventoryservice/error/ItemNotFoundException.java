package org.example.inventoryservice.error;

public class ItemNotFoundException extends  RuntimeException{

    public ItemNotFoundException(String skuCode){
        super("Item with SKU "+skuCode+" not found");
    }
}
