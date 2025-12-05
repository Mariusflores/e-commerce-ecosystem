package org.example.orderservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.datatype.OrderStatus;
import org.example.domain.dto.events.PaymentCompletedEvent;
import org.example.domain.dto.events.PaymentFailedEvent;
import org.example.orderservice.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final OrderService orderService;

    @RabbitListener(queues = "payment-completed-queue")
    public void handlePaymentCompleted(@Payload PaymentCompletedEvent event) {
        log.info("Received {} for Order {}", event.getClass().getSimpleName(), event.getOrderNumber());
        orderService.updateStatus(event.getOrderNumber(), OrderStatus.CONFIRMED);
    }

    @RabbitListener(queues = "payment-failed-queue")
    public void handlePaymentFailed(@Payload PaymentFailedEvent event) {
        log.info("Payment failed for order: {}, reason: {}",
                event.getOrderNumber(),
                event.getFailureReason());
        orderService.updateStatus(event.getOrderNumber(), OrderStatus.CANCELLED);

    }
}
