package org.example.paymentsservice.repository;

import org.example.paymentsservice.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findAllByCustomerId(Long customerId);

    List<Payment> findAllByOrderNumber(String orderNumber);
}
