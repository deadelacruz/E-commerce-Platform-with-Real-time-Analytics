package com.example.demo.presentation.controller;

import com.example.demo.application.service.OrderService;
import com.example.demo.domain.entity.Order;
import com.example.demo.domain.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Order REST Controller
 * Presentation Layer - Clean Architecture
 * Handles order-related HTTP requests and responses
 */
@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    
    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }
    
    /**
     * Get all orders with pagination
     * @param pageable Pagination parameters
     * @return Page of orders
     */
    @GetMapping
    public ResponseEntity<Page<Order>> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Get order by ID
     * @param id Order ID
     * @return Order if found, 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get orders by customer ID
     * @param customerId Customer ID
     * @param pageable Pagination parameters
     * @return Page of orders for the customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<Order>> getOrdersByCustomer(@PathVariable Long customerId, Pageable pageable) {
        Page<Order> orders = orderRepository.findByCustomerId(customerId, pageable);
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Get orders by status
     * @param status Order status
     * @param pageable Pagination parameters
     * @return Page of orders with the specified status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<Order>> getOrdersByStatus(@PathVariable Order.OrderStatus status, Pageable pageable) {
        Page<Order> orders = orderRepository.findByStatus(status, pageable);
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Get order by order number
     * @param orderNumber Order number
     * @return Order if found, 404 if not found
     */
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<Order> getOrderByNumber(@PathVariable String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }
    
    /**
     * Create a new order
     * @param order Order to create
     * @return Created order
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) {
        try {
            // Generate order number if not provided
            if (order.getOrderNumber() == null || order.getOrderNumber().trim().isEmpty()) {
                String orderNumber;
                do {
                    orderNumber = "ORD-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
                } while (orderRepository.existsByOrderNumber(orderNumber));
                order.setOrderNumber(orderNumber);
            }
            
            // Calculate total amount
            order.calculateTotalAmount();
            
            Order savedOrder = orderRepository.save(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Update order status
     * @param id Order ID
     * @param status New status
     * @return Updated order
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestParam Order.OrderStatus status) {
        try {
            Order order;
            switch (status) {
                case CONFIRMED:
                    order = orderService.confirmOrder(id);
                    break;
                case SHIPPED:
                    order = orderService.shipOrder(id);
                    break;
                case DELIVERED:
                    order = orderService.deliverOrder(id);
                    break;
                default:
                    // For other statuses, use direct repository access
                    Optional<Order> orderOpt = orderRepository.findById(id);
                    if (orderOpt.isEmpty()) {
                        return ResponseEntity.notFound().build();
                    }
                    order = orderOpt.get();
                    order.setStatus(status);
                    order = orderRepository.save(order);
                    break;
            }
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Cancel an order
     * @param id Order ID
     * @return Updated order
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
        try {
            Order order = orderService.cancelOrder(id);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get order statistics
     * @return Order statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getOrderStats() {
        OrderService.OrderStatistics stats = orderService.getOrderStatistics();
        Map<String, Object> response = new HashMap<>();
        response.put("totalOrders", stats.getTotalOrders());
        response.put("pendingOrders", stats.getPendingOrders());
        response.put("confirmedOrders", stats.getConfirmedOrders());
        response.put("shippedOrders", stats.getShippedOrders());
        response.put("deliveredOrders", stats.getDeliveredOrders());
        response.put("cancelledOrders", stats.getCancelledOrders());
        
        return ResponseEntity.ok(response);
    }
}
