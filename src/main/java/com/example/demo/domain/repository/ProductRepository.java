package com.example.demo.domain.repository;

import com.example.demo.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Product Repository Interface
 * Domain Layer - Repository Pattern
 * Follows Interface Segregation Principle (ISP)
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    
    /**
     * Find all active products with pagination
     * @param pageable Pagination information
     * @return Page of active products
     */
    Page<Product> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Find all distinct categories from active products
     * @return List of distinct categories
     */
    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.isActive = true ORDER BY p.category")
    List<String> findDistinctCategories();
    
    /**
     * Find products by category and active status with pagination
     * @param category Product category
     * @param pageable Pagination information
     * @return Page of products in the category
     */
    Page<Product> findByCategoryAndIsActiveTrue(String category, Pageable pageable);
    
    /**
     * Find products by name containing search term and active status with pagination
     * @param name Search term
     * @param pageable Pagination information
     * @return Page of products matching the search term
     */
    Page<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String name, Pageable pageable);
    
    /**
     * Find products by price range and active status with pagination
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @param pageable Pagination information
     * @return Page of products within price range
     */
    Page<Product> findByPriceBetweenAndIsActiveTrue(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice, Pageable pageable);
    
    /**
     * Find products with stock quantity greater than specified value and active status with pagination
     * @param stockQuantity Minimum stock quantity
     * @param pageable Pagination information
     * @return Page of products with stock above threshold
     */
    Page<Product> findByStockQuantityGreaterThanAndIsActiveTrue(Integer stockQuantity, Pageable pageable);
    
    /**
     * Find all active products (without pagination)
     * @return List of active products
     */
    List<Product> findByIsActiveTrue();
    
}
