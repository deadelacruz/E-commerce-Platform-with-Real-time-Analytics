package com.example.demo.presentation.controller;

import com.example.demo.application.service.ProductService;
import com.example.demo.application.usecase.product.CreateProductUseCase;
import com.example.demo.domain.entity.Product;
import com.example.demo.domain.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Product REST Controller
 * Presentation Layer - Clean Architecture
 * Handles HTTP requests and responses
 * Follows Single Responsibility Principle (SRP)
 */
@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*")
public class ProductController {
    
    private final ProductService productService;
    private final CreateProductUseCase createProductUseCase;
    
    public ProductController(ProductService productService, 
                           CreateProductUseCase createProductUseCase) {
        this.productService = productService;
        this.createProductUseCase = createProductUseCase;
    }
    
    /**
     * Get all products with pagination and filtering
     * @param pageable Pagination parameters
     * @param category Optional category filter
     * @param search Optional search term
     * @param minPrice Optional minimum price filter
     * @param maxPrice Optional maximum price filter
     * @param inStock Optional in stock filter
     * @return Page of products
     */
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            Pageable pageable,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) java.math.BigDecimal minPrice,
            @RequestParam(required = false) java.math.BigDecimal maxPrice,
            @RequestParam(required = false) Boolean inStock) {
        
        Page<Product> products = productService.getAllActiveProductsWithFilters(
            pageable, category, search, minPrice, maxPrice, inStock);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get product by ID
     * @param id Product ID
     * @return Product if found, 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Create a new product
     * @param request Product creation request
     * @return Created product
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody CreateProductUseCase.CreateProductRequest request) {
        try {
            Product product = createProductUseCase.execute(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Update an existing product
     * @param id Product ID
     * @param request Product update request
     * @return Updated product
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, 
                                                @Valid @RequestBody CreateProductUseCase.CreateProductRequest request) {
        Optional<Product> existingProduct = productService.getProductById(id);
        if (existingProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            Product product = existingProduct.get();
            product.setName(request.getName());
            product.setDescription(request.getDescription());
            product.setPrice(request.getPrice());
            product.setStockQuantity(request.getStockQuantity());
            product.setCategory(request.getCategory());
            if (request.getImageUrl() != null) {
                product.setImageUrl(request.getImageUrl());
            }
            
            Product updatedProduct = productService.updateProduct(product);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Delete a product (soft delete by deactivating)
     * @param id Product ID
     * @return Success response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deactivateProduct(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get all product categories
     * @return List of product categories
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = productService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    
    // Admin functions removed - not used by the frontend
    
    // DTO removed - no longer needed
}
