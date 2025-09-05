package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;


}
