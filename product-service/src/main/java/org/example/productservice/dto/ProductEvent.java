package org.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.common.datatype.Action;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductEvent {
    String skuCode;
    int quantity;
    Action action;
}
