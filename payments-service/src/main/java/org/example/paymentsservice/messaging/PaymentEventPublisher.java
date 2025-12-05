package org.example.paymentsservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.dto.events.PaymentCompletedEvent;
import org.example.domain.dto.events.PaymentFailedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    private final static String EXCHANGE_NAME = "payment-exchange";

    // Define Routing keys as constants
    public static final String PAYMENT_COMPLETED = "payment.completed";
    public static final String PAYMENT_FAILED = "payment.failed";



    public void publishPaymentCompletedEvent(PaymentCompletedEvent event){
        publishEvent(event, PAYMENT_COMPLETED);
    }

    public void publishPaymentFailedEvent(PaymentFailedEvent event){
        publishEvent(event, PAYMENT_FAILED);
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
