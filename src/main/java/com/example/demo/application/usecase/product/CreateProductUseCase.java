package com.example.demo.application.usecase.product;

import com.example.demo.domain.entity.Product;
import com.example.demo.domain.repository.ProductRepository;
import com.example.demo.application.service.ProductDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Create Product Use Case
 * Follows Single Responsibility Principle (SRP)
 * Application Layer - Clean Architecture
 */
@Service
public class CreateProductUseCase {
    
    private final ProductRepository productRepository;
    private final ProductDomainService productDomainService;
    
    public CreateProductUseCase(ProductRepository productRepository, 
                               ProductDomainService productDomainService) {
        this.productRepository = productRepository;
        this.productDomainService = productDomainService;
    }
    
    /**
     * Creates a new product
     * @param request Product creation request
     * @return Created product
     */
    @Transactional
    public Product execute(CreateProductRequest request) {
        // Validate business rules
        productDomainService.validateProductCreation(request);
        
        // Create product entity
        Product product = new Product(
            request.getName(),
            request.getDescription(),
            request.getPrice(),
            request.getStockQuantity(),
            request.getCategory()
        );
        
        if (request.getImageUrl() != null) {
            product.setImageUrl(request.getImageUrl());
        }
        
        // Save product
        return productRepository.save(product);
    }
    
    /**
     * Request DTO for creating a product
     */
    public static class CreateProductRequest {
        private String name;
        private String description;
        private java.math.BigDecimal price;
        private Integer stockQuantity;
        private String category;
        private String imageUrl;
        
        // Constructors
        public CreateProductRequest() {}
        
        public CreateProductRequest(String name, String description, 
                                  java.math.BigDecimal price, Integer stockQuantity, 
                                  String category, String imageUrl) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.stockQuantity = stockQuantity;
            this.category = category;
            this.imageUrl = imageUrl;
        }
        
        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public java.math.BigDecimal getPrice() { return price; }
        public void setPrice(java.math.BigDecimal price) { this.price = price; }
        
        public Integer getStockQuantity() { return stockQuantity; }
        public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
        
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    }
}
