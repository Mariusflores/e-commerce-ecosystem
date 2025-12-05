package org.example.productservice.controllers;


import lombok.RequiredArgsConstructor;
import org.example.domain.dto.ProductInfo;
import org.example.productservice.dto.ProductRequest;
import org.example.productservice.dto.ProductResponse;
import org.example.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Get Requests
     * */
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getProducts(){return productService.getAllProducts();}

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductById(@PathVariable String id){
        return productService.getProductById(id);
    }

    @GetMapping("/batch")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductInfo> getProductsBySkuCodes(@RequestParam List<String> skuCodes){
        return productService.getProductsBySkuCodes(skuCodes);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createProduct(@RequestBody ProductRequest productRequest){
        return productService.createProduct(productRequest);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateProduct(@PathVariable String id, @RequestBody  ProductRequest productRequest){
        productService.updateProduct(id, productRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProductById(@PathVariable String id){
        productService.deleteProductById(id);
    }

}
