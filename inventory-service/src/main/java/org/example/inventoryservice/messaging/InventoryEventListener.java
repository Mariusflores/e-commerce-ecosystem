package org.example.inventoryservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.datatype.Action;
import org.example.domain.dto.events.InventoryEvent;
import org.example.domain.dto.events.OrderPlacedEvent;
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
     *
     */
    @RabbitListener(queues = "product-queue")
    public void handleInventoryEvent(@Payload InventoryEvent event) {
        log.info("Processing Event: {} - For item {}",
                event.getAction(),
                event.getSkuCode()
        );
        handleEvent(event);
    }

    @RabbitListener(queues = "inventory-order-queue")
    public void handleOrderPlacedEvent(@Payload OrderPlacedEvent event) {
        log.info("Processing order: {} - reducing stock for {} items",
                event.getOrderNumber(),
                event.getItems().size()
        );

        for (OrderPlacedEvent.OrderItemDto item : event.getItems()) {
            InventoryEvent inventoryEvent = InventoryEvent.builder()
                    .skuCode(item.getSkuCode())
                    .quantity(item.getQuantity())
                    .action(Action.REDUCE)
                    .build();

            handleEvent(inventoryEvent);
        }
    }

    public void handleEvent(InventoryEvent event) {
        inventoryService.handleInventoryEvent(event);
    }
}
