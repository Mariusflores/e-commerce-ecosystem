package org.example.orderservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.dto.OrderStatusChangedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    private final static String EXCHANGE_NAME = "order_exchange";

    /**
     * Use Rabbit Template to send an event to 'product-exchange' with given routing key
     * */
    public void publishStatusCodeEvent(OrderStatusChangedEvent event, String routingKey) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, routingKey, event);
        log.info("event {} has been published", event);
    }
}
