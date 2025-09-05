package org.example.inventoryservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ItemNotFoundException extends  RuntimeException{

    public ItemNotFoundException(String skuCode){
        super("Item with SKU "+skuCode+" not found");
    }
}
