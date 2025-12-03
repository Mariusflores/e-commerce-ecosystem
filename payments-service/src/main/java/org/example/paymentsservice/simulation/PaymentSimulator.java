package org.example.paymentsservice.simulation;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.datatype.PaymentStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Slf4j
public class PaymentSimulator {

    private static final String[] FAILURE_REASONS = {
            "INSUFFICIENT_FUNDS",
            "CARD_DECLINED",
            "NETWORK_ERROR",
            "GATEWAY_TIMEOUT"
    };

    public PaymentSimulationResult simulatePayment(
            BigDecimal amount,
            String paymentMethod){

        log.info("Simulating payment for {} via {}",amount, paymentMethod);

        simulateDelay();
        PaymentStatus status = simulatePaymentStatus(paymentMethod);

        return PaymentSimulationResult.builder()
                .paymentStatus(status)
                .transactionId(generateTransactionId())
                .failureReason(status ==
                        PaymentStatus.FAILED ? simulateFailureReason() : null
                )
                .build();

    }

    /**
     * Helper methods
     * */
    private String generateTransactionId(){
        return "PAY-" + System.currentTimeMillis() + "-" +
                UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private PaymentStatus simulatePaymentStatus(String paymentMethod){
        int failureChance = switch (paymentMethod.toUpperCase()){
            case "CREDIT_CARD" -> 5; // 20% failure chance
            case "DEBIT_CARD" -> 10; // 10% failure chance
            case "PAYPAL" -> 20; // 5% failure chance
            default -> 5;
        };
        int random = (int) (Math.random() * failureChance) + 1;

        return random == 1 ? PaymentStatus.FAILED : PaymentStatus.COMPLETED;
    }

    private String simulateFailureReason(){
        int random = (int) (Math.random() * FAILURE_REASONS.length);
        return FAILURE_REASONS[random];
    }

    private void simulateDelay(){
        // Simulate 1-4 second delay
        int delay = ThreadLocalRandom.current().nextInt(1000, 4001);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
