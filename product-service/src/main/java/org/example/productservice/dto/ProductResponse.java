package org.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String id;
    private String skuCode;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
