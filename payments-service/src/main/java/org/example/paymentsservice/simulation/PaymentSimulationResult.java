package org.example.paymentsservice.simulation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.datatype.PaymentStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentSimulationResult {

    private PaymentStatus paymentStatus;
    private String transactionId;
    private String failureReason;
}
