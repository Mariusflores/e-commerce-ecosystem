package org.example.paymentsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.dto.events.OrderPlacedEvent;
import org.example.paymentsservice.repository.PaymentRepository;
import org.example.paymentsservice.simulation.PaymentSimulator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentSimulator simulator;

    public void processPayment(OrderPlacedEvent order) {


    }
}
