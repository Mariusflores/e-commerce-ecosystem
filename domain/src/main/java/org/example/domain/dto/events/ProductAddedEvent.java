package org.example.domain.dto.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.datatype.Action;

/**
 * Data Transfer Object for RabbitMQ Event transferring
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductAddedEvent {
    String skuCode;
    int quantity;
    Action action;
}
