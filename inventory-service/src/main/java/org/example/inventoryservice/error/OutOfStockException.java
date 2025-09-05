package org.example.inventoryservice.error;

public class OutOfStockException extends RuntimeException {


    public OutOfStockException(String message) {
        super(message);
    }
}
