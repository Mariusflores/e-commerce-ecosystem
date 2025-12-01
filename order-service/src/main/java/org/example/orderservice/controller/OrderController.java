package org.example.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.dto.OrderRequest;
import org.example.orderservice.dto.OrderResponse;
import org.example.orderservice.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public void createOrder(@RequestBody OrderRequest order){
        orderService.createOrder(order);
    }

    @GetMapping("/{orderNumber}")
    public OrderResponse fetchOrder(@PathVariable String orderNumber){

        return orderService.fetchByOrderNumber(orderNumber);
    }
    @GetMapping
    public List<OrderResponse> fetchAllOrders(@RequestParam String customerId){
        return orderService.fetchByCustomerId(customerId);
    }

    @DeleteMapping("/{orderNumber")
    public void deleteOrder(@PathVariable String orderNumber){

    }
}
