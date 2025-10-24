package com.example.demo.application.service;

import com.example.demo.application.usecase.product.CreateProductUseCase;
import org.springframework.stereotype.Service;

/**
 * Product Domain Service
 * Contains business logic that doesn't belong to a single entity
 * Follows Domain-Driven Design principles
 */
@Service
public class ProductDomainService {
    
    /**
     * Validates product creation business rules
     * @param request Product creation request
     * @throws IllegalArgumentException if validation fails
     */
    public void validateProductCreation(CreateProductUseCase.CreateProductRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Product request cannot be null");
        }
        
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
        
        if (request.getPrice() == null || request.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero");
        }
        
        if (request.getStockQuantity() == null || request.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        
        if (request.getCategory() == null || request.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Product category is required");
        }
    }
    
}
