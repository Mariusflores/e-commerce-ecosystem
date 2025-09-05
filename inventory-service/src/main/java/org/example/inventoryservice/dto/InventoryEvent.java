package org.example.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.datatype.Action;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryEvent {

    String skuCode;
    int quantity;
    Action action;
}
