package org.example.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.datatype.Action;
import org.example.domain.dto.ProductAddedEvent;
import org.example.productservice.exception.ProductNotFoundException;
import org.example.productservice.messaging.ProductEventPublisher;
import org.example.productservice.dto.ProductRequest;
import org.example.productservice.dto.ProductResponse;
import org.example.productservice.model.Product;
import org.example.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository repository;
    private final ProductEventPublisher eventPublisher;

    /**
     * Converts ProductRequest to Product Document
     * Saves Product to DB
     * publishes a creation event via ProductEventPublisher
     * */
    public String createProduct(ProductRequest request){
        Product product = Product.builder()
                .name(request.getName())
                .skuCode(generateSku(request))
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .build();

        Product savedProduct = repository.save(product);

        log.info("Product {} created", product.getId());

        ProductAddedEvent event = ProductAddedEvent.builder()
                .skuCode(savedProduct.getSkuCode())
                .quantity(10)
                .action(Action.CREATE)
                .build();

        eventPublisher.publishProductEvent(event, "product.created");

        return savedProduct.getId();
    }

    /**
     * Finds product by its ID and returns a ProductResponse DTO
     * Throws RuntimeException if the product is not found
     * */
    public ProductResponse getProductById(String id){
        Product product = repository.findById(id).
                orElseThrow(() -> new RuntimeException("Product with id: " + id +" not found"));
        return mapToProductResponse(product);
    }


    /**
     * Returns a list of all products in the database as ProductResponse DTOs
     * */
    public List<ProductResponse> getAllProducts(){
        List<Product> products = repository.findAll();

        return products.stream().map(this::mapToProductResponse).toList();
    }

    /**
     * Updates one or more fields of a product document identified by ID
     * Fields in ProductRequest that are null or equal to the existing values are ignored.
     * Throws custom ProductNotFoundException if the product is not found
     * */
    public void updateProduct(String id, ProductRequest request){
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " not found"));

        if((request.getName() != null) && !Objects.equals(request.getName(), product.getName())) {
            product.setName(request.getName());
        }
        if( request.getDescription() != null && !Objects.equals(request.getDescription() ,product.getDescription())) {
            product.setDescription(request.getDescription());
        }
        if(request.getPrice() != null && !Objects.equals(request.getPrice(),product.getPrice())) {
            product.setPrice(request.getPrice());
        }
        if(request.getCategory() != null && !Objects.equals(request.getCategory(), product.getCategory())) {
            product.setCategory(request.getCategory());
        }


        repository.save(product);
        log.info("Product {} updated", product.getId());
    }

    /**
     * Deletes a product document by ID and publishes deletion event via ProductEventPublisher
     * Throws custom ProductNotFoundException if the product is not found.
     * */
    public void deleteProductById(String id){
        Product product = repository.findById(id).
                orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " not found"));
        repository.delete(product);

        log.info("Product {} deleted", id);

        ProductAddedEvent event = ProductAddedEvent.builder()
                .skuCode(product.getSkuCode())
                .quantity(0)
                .action(Action.DELETE)
                .build();

        eventPublisher.publishProductEvent(event, "product.deleted");
    }

    /**
     * Converts Product document to ProductResponse DTO
     * */
    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .skuCode(product.getSkuCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    /**
     * Generates a SKU code for product based on its category, name, and current timestamp
     * */
    public String generateSku(ProductRequest request) {
        String categoryCode = request.getCategory().substring(0, 3).toUpperCase();
        String shortName = request.getName().substring(0, 2).toUpperCase();
        String timePart = String.valueOf(System.currentTimeMillis()).substring(7);
        return categoryCode + "-" + shortName + "-" + timePart;
    }




}
