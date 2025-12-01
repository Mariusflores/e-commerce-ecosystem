package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.datatype.OrderStatus;
import org.example.orderservice.dto.OrderRequest;
import org.example.orderservice.dto.OrderResponse;
import org.example.orderservice.messaging.OrderEventPublisher;
import org.example.orderservice.model.CustomerOrder;
import org.example.orderservice.model.OrderItem;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderEventPublisher eventPublisher;


    public void createOrder(OrderRequest requestOrder) {
        CustomerOrder order = CustomerOrder.builder()
                .customerId(requestOrder.getCustomerId())
                .build();

        for (OrderItem orderItem : requestOrder.getItems()) {
            order.addItem(orderItem);
        }
        orderRepository.save(order);
    }

    public OrderResponse fetchByOrderNumber(String orderNumber) {
        CustomerOrder order = orderRepository.findByOrderNumber(orderNumber);
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerId(order.getCustomerId())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .total(order.getTotal())
                .build();
    }

    public List<OrderResponse> fetchByCustomerId(String customerId) {

        List<CustomerOrder> orders = orderRepository.getAllByCustomerId(customerId);
        return orders.stream().map(this::mapToOrderResponse).toList();
    }

    public void updateStatus(String orderNumber, OrderStatus status) {
        CustomerOrder order = orderRepository.findByOrderNumber(orderNumber);

        order.setOrderStatus(status);
        orderRepository.save(order);

    }

    private OrderResponse mapToOrderResponse(CustomerOrder customerOrder) {
        return OrderResponse.builder()
                .id(customerOrder.getId())
                .orderNumber(customerOrder.getOrderNumber())
                .customerId(customerOrder.getCustomerId())
                .orderDate(customerOrder.getOrderDate())
                .orderStatus(customerOrder.getOrderStatus())
                .total(customerOrder.getTotal())
                .build();
    }


}
