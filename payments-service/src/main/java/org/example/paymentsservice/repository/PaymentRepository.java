package org.example.paymentsservice.repository;

import org.example.paymentsservice.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
