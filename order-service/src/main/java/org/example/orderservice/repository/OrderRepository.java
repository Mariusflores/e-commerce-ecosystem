package org.example.orderservice.repository;

import org.example.orderservice.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {


    CustomerOrder findByOrderNumber(String orderNumber);

    List<CustomerOrder> getAllByCustomerId(String customerId);
}
