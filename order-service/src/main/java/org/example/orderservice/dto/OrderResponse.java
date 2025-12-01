package org.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.datatype.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    Long id;
    String customerId;
    String orderNumber;
    LocalDateTime orderDate;
    OrderStatus orderStatus;
    BigDecimal total;
    private String shippingAddress;


}
