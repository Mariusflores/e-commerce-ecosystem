package org.example.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.dto.OrderRequest;
import org.example.orderservice.dto.OrderResponse;
import org.example.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createOrder(@RequestBody OrderRequest order) {
        return orderService.createOrder(order);
    }

    @GetMapping("/{orderNumber}")
    public OrderResponse fetchOrder(@PathVariable String orderNumber) {

        return orderService.fetchByOrderNumber(orderNumber);
    }

    @GetMapping
    public List<OrderResponse> fetchAllOrders(@RequestParam Long customerId) {
        return orderService.fetchByCustomerId(customerId);
    }

    @DeleteMapping("/{orderNumber}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteOrder(@PathVariable String orderNumber) {
        orderService.deleteOrder(orderNumber);
    }
}
