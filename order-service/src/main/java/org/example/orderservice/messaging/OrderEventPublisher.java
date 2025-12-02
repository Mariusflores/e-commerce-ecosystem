package org.example.orderservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.dto.events.OrderPlacedEvent;
import org.example.domain.dto.events.OrderStatusChangedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    private final static String EXCHANGE_NAME = "order-exchange";

    // Define Routing keys as constants
    public static final String ORDER_STATUS_UPDATED = "order.status.updated";
    public static final String ORDER_PLACED = "order.placed";


    public void publishStatusUpdateEvent(OrderStatusChangedEvent event){
        publishEvent(event, ORDER_STATUS_UPDATED);
    }

    public void publishOrderPlacedEvent(OrderPlacedEvent event){
        publishEvent(event, ORDER_PLACED);
    }
    /**
     * Generic Publish Event method
     * */
    public void publishEvent(Object event, String routingKey) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, routingKey, event);
        log.info("event {} has been published to exchange {} with routing key {}",
                event.getClass().getSimpleName(), EXCHANGE_NAME, routingKey);
    }



}
