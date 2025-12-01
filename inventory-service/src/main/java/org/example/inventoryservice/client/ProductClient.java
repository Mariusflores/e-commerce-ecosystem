package org.example.inventoryservice.client;

import org.example.domain.dto.ProductInfo;
import org.example.inventoryservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "product-service",
        configuration = FeignConfig.class
)
public interface ProductClient {
    @GetMapping("/api/product/batch")
    List<ProductInfo> getItemsBySkuCodes(@RequestParam List<String> skuCodes);
}
