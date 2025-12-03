package org.example.domain.dto.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPlacedEvent {

    // Order identification
    private Long orderId;
    private String orderNumber;

    // Customer information
    private Long customerId;
    //TODO private String customerEmail once user service is up

    // Order details
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;

    // Items for inventory management
    private List<OrderItemDto> items;

    // Payment information
    private String paymentMethod;

    // Shipping information
    private String shippingAddress;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemDto {
        private String productName;
        private String skuCode;
        private Integer quantity;
        private BigDecimal price;
        private Long orderId;
    }
}