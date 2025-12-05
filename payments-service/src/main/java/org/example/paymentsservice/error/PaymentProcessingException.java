package org.example.paymentsservice.error;

public class PaymentProcessingException extends RuntimeException {
    public PaymentProcessingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
