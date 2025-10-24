package com.example.demo.application.service;

import com.example.demo.domain.entity.Product;
import com.example.demo.domain.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Product Application Service
 * Application Layer - Service Layer
 * Handles product-related business operations
 */
@Service
@Transactional
public class ProductService {
    
    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    /**
     * Get all active products with pagination
     * @param pageable Pagination parameters
     * @return Page of active products
     */
    @Transactional(readOnly = true)
    public Page<Product> getAllActiveProducts(Pageable pageable) {
        return productRepository.findByIsActiveTrue(pageable);
    }
    
    /**
     * Get all active products with pagination and filtering
     * @param pageable Pagination parameters
     * @param category Optional category filter
     * @param search Optional search term
     * @param minPrice Optional minimum price filter
     * @param maxPrice Optional maximum price filter
     * @param inStock Optional in stock filter
     * @return Page of filtered products
     */
    @Transactional(readOnly = true)
    public Page<Product> getAllActiveProductsWithFilters(
            Pageable pageable, 
            String category, 
            String search, 
            java.math.BigDecimal minPrice, 
            java.math.BigDecimal maxPrice, 
            Boolean inStock) {
        
        try {
            // For now, implement basic filtering logic
            // In a real application, you'd use Spring Data JPA Specifications or custom queries
            
            // Check if any filters are applied
            boolean hasFilters = (category != null && !category.trim().isEmpty()) ||
                               (search != null && !search.trim().isEmpty()) ||
                               (minPrice != null || maxPrice != null) ||
                               (inStock != null && inStock);
            
            if (!hasFilters) {
                // No filters applied, return all active products
                return productRepository.findByIsActiveTrue(pageable);
            }
            
            // Apply filters in order of specificity
            // Note: This is a simplified approach. In production, you'd use JPA Specifications
            // or custom queries to combine multiple filters efficiently
            
            if (category != null && !category.trim().isEmpty()) {
                // Category filter takes precedence for now
                // In a real app, you'd combine this with other filters
                return productRepository.findByCategoryAndIsActiveTrue(category, pageable);
            }
            
            if (search != null && !search.trim().isEmpty()) {
                // Search filter
                return productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(search, pageable);
            }
            
            if (minPrice != null || maxPrice != null) {
                // Price range filter
                java.math.BigDecimal min = minPrice != null ? minPrice : java.math.BigDecimal.ZERO;
                java.math.BigDecimal max = maxPrice != null ? maxPrice : new java.math.BigDecimal("999999");
                return productRepository.findByPriceBetweenAndIsActiveTrue(min, max, pageable);
            }
            
            if (inStock != null && inStock) {
                // Stock filter
                return productRepository.findByStockQuantityGreaterThanAndIsActiveTrue(0, pageable);
            }
            
            // Default: return all active products
            return productRepository.findByIsActiveTrue(pageable);
        } catch (Exception e) {
            // Return empty page on error
            return org.springframework.data.domain.Page.empty(pageable);
        }
    }
    
    
    /**
     * Get product by ID
     * @param id Product ID
     * @return Product if found
     */
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    
    /**
     * Get all unique categories from actual products in database
     * @return List of unique categories
     */
    @Transactional(readOnly = true)
    public List<String> getAllCategories() {
        return productRepository.findDistinctCategories();
    }
    
    /**
     * Update an existing product
     * @param product Product to update
     * @return Updated product
     */
    @Transactional
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }
    
    /**
     * Deactivate a product (soft delete)
     * @param id Product ID
     */
    @Transactional
    public void deactivateProduct(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found: " + id);
        }
        
        Product product = productOpt.get();
        product.setIsActive(false);
        productRepository.save(product);
    }
    
}

