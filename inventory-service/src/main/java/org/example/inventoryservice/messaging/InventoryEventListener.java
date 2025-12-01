package org.example.inventoryservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.dto.ProductAddedEvent;
import org.example.inventoryservice.services.InventoryService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryEventListener {

    private final InventoryService inventoryService;

    /**
     * Listens to the 'inventory-queue' for inventory events
     * and delegates them to InventoryService for handling.
     * */
    @RabbitListener(queues = "inventory-queue")
    public void handleInventoryEvent(@Payload ProductAddedEvent event){
        log.info("Received Inventory Event: {}", event);
        inventoryService.handleInventoryEvent(event);
    }
}
