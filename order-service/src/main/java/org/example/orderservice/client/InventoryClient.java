package org.example.orderservice.client;

import org.example.domain.dto.ProductInfo;
import org.example.orderservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "inventory-service",
        configuration = FeignConfig.class
)
public interface InventoryClient {

    @GetMapping("/api/inventory/batch")
    List<ProductInfo> getItemsBySkuCodes(@RequestParam List<String> skuCodes);
}
