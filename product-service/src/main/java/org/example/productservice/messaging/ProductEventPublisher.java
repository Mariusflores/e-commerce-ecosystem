package org.example.productservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.dto.events.ProductAddedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductEventPublisher {

    // Inject Rabbit Template
    private final RabbitTemplate rabbitTemplate;


    private static final String EXCHANGE_NAME = "product-exchange";


    /**
     * Use Rabbit Template to send an event to 'product-exchange' with given routing key
     * */
    public void publishProductEvent(ProductAddedEvent event, String routingKey) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, routingKey, event);
        log.info("event {} has been published", event);
    }
}
