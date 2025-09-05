package org.example.productservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.productservice.dto.ProductEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductEventPublisher {

    private final RabbitTemplate rabbitTemplate;


    private static final String EXCHANGE_NAME = "product-exchange";
    private static final String ROUTING_KEY = "product.created";

    //Send method
    public void publishProductEvent(ProductEvent event){
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, event);
        log.info("event {} has been published", event);
    }
}
