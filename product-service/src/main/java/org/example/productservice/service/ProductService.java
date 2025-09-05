package org.example.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.datatype.Action;
import org.example.productservice.dto.ProductEvent;
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

        ProductEvent event = ProductEvent.builder()
                .skuCode(savedProduct.getSkuCode())
                .quantity(10)
                .action(Action.CREATE)
                .build();

        eventPublisher.publishProductEvent(event);

        return savedProduct.getId();
    }

    public ProductResponse getProductById(String id){
        Product product = repository.findById(id).
                orElseThrow(() -> new RuntimeException("Product with id: " + id +" not found"));
        return mapToProductResponse(product);
    }



    public List<ProductResponse> getAllProducts(){
        List<Product> products = repository.findAll();

        return products.stream().map(this::mapToProductResponse).toList();
    }

    public void updateProduct(String id, ProductRequest request){
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

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

    public void deleteProductById(String id){
        Product product = repository.findById(id).
                orElseThrow(() -> new RuntimeException("Product with id: " + id +" not found"));
        repository.delete(product);
    }

    /*
    * Helpers
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

    public String generateSku(ProductRequest request) {
        String categoryCode = request.getCategory().substring(0, 3).toUpperCase();
        String shortName = request.getName().substring(0, 2).toUpperCase();
        String timePart = String.valueOf(System.currentTimeMillis()).substring(7);
        return categoryCode + "-" + shortName + "-" + timePart;
    }




}
