package org.example.paymentsservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.dto.events.OrderPlacedEvent;
import org.example.domain.dto.events.PaymentCompletedEvent;
import org.example.domain.dto.events.PaymentFailedEvent;
import org.example.paymentsservice.messaging.PaymentEventPublisher;
import org.example.paymentsservice.models.Payment;
import org.example.paymentsservice.repository.PaymentRepository;
import org.example.paymentsservice.simulation.PaymentSimulationResult;
import org.example.paymentsservice.simulation.PaymentSimulator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentSimulator simulator;
    private final PaymentEventPublisher eventPublisher;

    @Transactional
    public void processPayment(OrderPlacedEvent order) {
        PaymentSimulationResult paymentResult = simulator.simulatePayment(
                order.getTotalAmount(),
                order.getPaymentMethod()
        );

        Payment payment = Payment.builder()
                .orderNumber(order.getOrderNumber())
                .customerId(order.getCustomerId())
                .amount(order.getTotalAmount())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(paymentResult.getPaymentStatus())
                .transactionId(paymentResult.getTransactionId())
                .failureReason(paymentResult.getFailureReason())
                .build();

        paymentRepository.save(payment);

        // Publish event
        switch (payment.getPaymentStatus()) {
            case COMPLETED -> eventPublisher.publishPaymentCompletedEvent(mapToPaymentCompletedEvent(payment));
            case FAILED -> eventPublisher.publishPaymentFailedEvent(mapToPaymentFailedEvent(payment));
        }

    }

    private PaymentFailedEvent mapToPaymentFailedEvent(Payment payment) {
        return PaymentFailedEvent.builder()
                .orderNumber(payment.getOrderNumber())
                .transactionId(payment.getTransactionId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .failureReason(payment.getFailureReason())
                .failedAt(payment.getCreatedDate())

                .build();
    }

    private PaymentCompletedEvent mapToPaymentCompletedEvent(Payment payment) {
        return PaymentCompletedEvent.builder()
                .orderNumber(payment.getOrderNumber())
                .transactionId(payment.getTransactionId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .completedAt(payment.getCreatedDate())
                .build();
    }
}
