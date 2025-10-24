package com.example.demo.domain.repository;

import com.example.demo.domain.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Order Repository Interface
 * Domain Layer - Repository Pattern
 * Follows Interface Segregation Principle (ISP)
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * Find orders by customer ID
     * @param customerId Customer ID
     * @return List of orders for the customer
     */
    List<Order> findByCustomerId(Long customerId);
    
    /**
     * Find orders by status
     * @param status Order status
     * @return List of orders with the specified status
     */
    List<Order> findByStatus(Order.OrderStatus status);
    
    
    /**
     * Find orders by order number
     * @param orderNumber Order number
     * @return Order if found
     */
    Order findByOrderNumber(String orderNumber);
    
    /**
     * Find orders by date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of orders within date range
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findByDateRange(@Param("startDate") java.time.LocalDateTime startDate, 
                               @Param("endDate") java.time.LocalDateTime endDate);
    
    /**
     * Find orders by customer ID with pagination
     * @param customerId Customer ID
     * @param pageable Pagination information
     * @return Page of orders for the customer
     */
    Page<Order> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Find orders by status with pagination
     * @param status Order status
     * @param pageable Pagination information
     * @return Page of orders with the specified status
     */
    Page<Order> findByStatus(Order.OrderStatus status, Pageable pageable);
    
    /**
     * Count orders by status
     * @param status Order status
     * @return Number of orders with the specified status
     */
    long countByStatus(Order.OrderStatus status);
    
    /**
     * Count orders by customer ID
     * @param customerId Customer ID
     * @return Number of orders for the customer
     */
    long countByCustomerId(Long customerId);
    
    /**
     * Find recent orders (last N days)
     * @param days Number of days
     * @return List of recent orders
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt >= :cutoffDate ORDER BY o.createdAt DESC")
    List<Order> findRecentOrders(@Param("cutoffDate") java.time.LocalDateTime cutoffDate);
    
    
    /**
     * Check if order exists by order number
     * @param orderNumber Order number
     * @return True if order exists with the order number
     */
    boolean existsByOrderNumber(String orderNumber);
}
