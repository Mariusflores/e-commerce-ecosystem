package org.example.productservice.repository;

import org.example.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    boolean existsBySkuCode(String skuCode);
    List<Product> findBySkuCodeIn(List<String> skuCodes);  // For batch queries
}
