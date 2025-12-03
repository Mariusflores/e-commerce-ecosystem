package org.example.paymentsservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.dto.events.OrderPlacedEvent;
import org.example.paymentsservice.service.PaymentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {

    private final PaymentService paymentService;

    @RabbitListener(queues = "order_queue")
    public void processPayment(OrderPlacedEvent order) {
        log.info("Received order placed event: {}", order );

        paymentService.processPayment(order);
    }
}
