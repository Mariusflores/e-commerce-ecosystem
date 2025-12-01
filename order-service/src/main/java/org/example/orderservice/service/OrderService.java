package org.example.orderservice.service;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.datatype.OrderStatus;
import org.example.domain.dto.ProductInfo;
import org.example.domain.dto.events.OrderPlacedEvent;
import org.example.orderservice.client.InventoryClient;
import org.example.orderservice.dto.OrderItemRequest;
import org.example.orderservice.dto.OrderRequest;
import org.example.orderservice.dto.OrderResponse;
import org.example.orderservice.messaging.OrderEventPublisher;
import org.example.orderservice.model.CustomerOrder;
import org.example.orderservice.model.OrderItem;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final InventoryClient inventoryClient;
    private final OrderRepository orderRepository;
    private final OrderEventPublisher eventPublisher;


    @Transactional
    public String createOrder(OrderRequest request) {

        log.info("Creating Order for customer: {}", request.getCustomerId());

        // Extract sku codes

        List<String> skuCodes = request.getItems().stream()
                .map(OrderItemRequest::getSkuCode)
                .toList();

        // Fetch Details from Inventory Service
        List<ProductInfo> products;

        try{
            products = inventoryClient.getItemsBySkuCodes(skuCodes);
        }catch (FeignException.NotFound e){
            log.error("One or more products not found in inventory");
            throw new RuntimeException("Product not found");
        }catch (FeignException e){
            log.error("Error communicating with inventory", e);
            throw new RuntimeException("Error communicating with inventory");
        }

        // Create a map for lookup
        Map<String, ProductInfo> productInfoMap = products.stream()
                .collect(Collectors.toMap(ProductInfo::getSkuCode, product -> product));

        //Validate and build order items
        List<OrderItem>  orderItems = new ArrayList<>();

        for(OrderItemRequest itemRequest : request.getItems()){
            ProductInfo product =  productInfoMap.get(itemRequest.getSkuCode());


            if(product == null){
                throw new RuntimeException("Product not found" + itemRequest.getSkuCode());
            }

            //validate stock availability

            if(product.getQuantity() < itemRequest.getQuantity()){
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }


            // Build order item
            OrderItem orderItem = OrderItem.builder()
                    .skuCode(product.getSkuCode())
                    .productName(product.getName())
                    .quantity(product.getQuantity())
                    .price(product.getPrice())
                    .category(product.getCategory())
                    .build();

            orderItems.add(orderItem);
        }

        //Create and save order

        CustomerOrder order = CustomerOrder.builder()
                .customerId(request.getCustomerId())
                .paymentMethod(request.getPaymentMethod())
                .shippingAddress(request.getShippingAddress())
                .build();

        for (OrderItem item: orderItems) {
            order.addItem(item);
        }
        CustomerOrder savedOrder = orderRepository.save(order);
        log.info("Saved Order for customer: {}", savedOrder.getCustomerId());

        /*
        * TODO (Publish Event to Payments)
        *  Requires Payments Service
        * */

        OrderPlacedEvent event = mapToOrderPlacedEvent(savedOrder);
        eventPublisher.publishOrderPlacedEvent(event);

        return savedOrder.getOrderNumber();
    }

    public OrderResponse fetchByOrderNumber(String orderNumber) {
        CustomerOrder order = orderRepository.findByOrderNumber(orderNumber);
        return mapToOrderResponse(order);
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


    public void deleteOrder(String orderNumber) {
        orderRepository.delete(orderRepository.findByOrderNumber(orderNumber));
    }

    /**
     * Helper Functions
     * */

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

    private OrderPlacedEvent mapToOrderPlacedEvent(CustomerOrder savedOrder) {
        return OrderPlacedEvent.builder()
                .orderId(savedOrder.getId())
                .orderNumber(savedOrder.getOrderNumber())
                .customerId(savedOrder.getCustomerId())
                .totalAmount(savedOrder.getTotal())
                .orderDate(savedOrder.getOrderDate())
                .items(savedOrder.getItems()
                        .stream()
                        .map(this::mapToOrderItemDto)
                        .toList())
                .paymentMethod(savedOrder.getPaymentMethod())
                .shippingAddress(savedOrder.getShippingAddress())
                .build();
    }

    private OrderPlacedEvent.OrderItemDto mapToOrderItemDto(OrderItem orderItem) {
        return OrderPlacedEvent.OrderItemDto.builder()
                .productName(orderItem.getProductName())
                .skuCode(orderItem.getSkuCode())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .orderId(orderItem.getOrder().getId())
                .build();
    }
}
