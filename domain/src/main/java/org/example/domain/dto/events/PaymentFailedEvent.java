package org.example.domain.dto.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentFailedEvent {
    private String orderNumber;
    private String transactionId;
    private BigDecimal amount;
    private String paymentMethod;
    private String failureReason;
    private LocalDateTime failedAt;
}
