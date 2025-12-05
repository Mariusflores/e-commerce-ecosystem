package org.example.paymentsservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.paymentsservice.dto.PaymentResponse;
import org.example.paymentsservice.service.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentsController {

    private final PaymentService paymentService;

    @GetMapping("/{orderNumber}")
    public List<PaymentResponse> getByOrderNumber(@PathVariable String orderNumber) {
        return paymentService.getPaymentByOrderNumber(orderNumber);
    }

    @GetMapping("/customer/{customerId}")
    public List<PaymentResponse> getByCustomerId(@PathVariable Long customerId) {
        return paymentService.getPaymentByCustomerId(customerId);

    }
}
