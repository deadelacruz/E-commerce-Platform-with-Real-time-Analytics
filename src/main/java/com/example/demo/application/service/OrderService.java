package com.example.demo.application.service;

import com.example.demo.domain.entity.Order;
import com.example.demo.domain.entity.OrderItem;
import com.example.demo.domain.entity.Product;
import com.example.demo.domain.repository.OrderRepository;
import com.example.demo.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Order Application Service
 * Application Layer - Service Layer
 * Handles order-related business operations
 */
@Service
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }
    
    /**
     * Create a new order with order items
     * @param customerId Customer ID
     * @param shippingAddress Shipping address
     * @param billingAddress Billing address
     * @param orderItems List of order items
     * @return Created order
     */
    public Order createOrder(Long customerId, String shippingAddress, String billingAddress, 
                           List<OrderItemRequest> orderItems) {
        
        // Generate order number
        String orderNumber = "ORD-" + System.currentTimeMillis();
        
        // Create order
        Order order = new Order(orderNumber, customerId, shippingAddress, billingAddress);
        
        // Process order items
        for (OrderItemRequest itemRequest : orderItems) {
            Optional<Product> productOpt = productRepository.findById(itemRequest.getProductId());
            if (productOpt.isEmpty()) {
                throw new IllegalArgumentException("Product not found: " + itemRequest.getProductId());
            }
            
            Product product = productOpt.get();
            
            // Check stock availability
            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }
            
            // Create order item
            OrderItem orderItem = new OrderItem(
                product.getId(),
                product.getName(),
                itemRequest.getQuantity(),
                product.getPrice()
            );
            
            order.addOrderItem(orderItem);
            
            // Reduce stock
            product.reduceStock(itemRequest.getQuantity());
            productRepository.save(product);
        }
        
        // Calculate total amount
        order.calculateTotalAmount();
        
        return orderRepository.save(order);
    }
    
    /**
     * Confirm an order
     * @param orderId Order ID
     * @return Confirmed order
     */
    public Order confirmOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        
        Order order = orderOpt.get();
        order.confirm();
        return orderRepository.save(order);
    }
    
    /**
     * Ship an order
     * @param orderId Order ID
     * @return Shipped order
     */
    public Order shipOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        
        Order order = orderOpt.get();
        order.ship();
        return orderRepository.save(order);
    }
    
    /**
     * Deliver an order
     * @param orderId Order ID
     * @return Delivered order
     */
    public Order deliverOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        
        Order order = orderOpt.get();
        order.deliver();
        return orderRepository.save(order);
    }
    
    /**
     * Cancel an order and restore stock
     * @param orderId Order ID
     * @return Cancelled order
     */
    public Order cancelOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        
        Order order = orderOpt.get();
        
        // Restore stock for each order item
        for (OrderItem orderItem : order.getOrderItems()) {
            Optional<Product> productOpt = productRepository.findById(orderItem.getProductId());
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                product.addStock(orderItem.getQuantity());
                productRepository.save(product);
            }
        }
        
        order.cancel();
        return orderRepository.save(order);
    }
    
    /**
     * Get order statistics
     * @return Order statistics
     */
    @Transactional(readOnly = true)
    public OrderStatistics getOrderStatistics() {
        long totalOrders = orderRepository.count();
        long pendingOrders = orderRepository.countByStatus(Order.OrderStatus.PENDING);
        long confirmedOrders = orderRepository.countByStatus(Order.OrderStatus.CONFIRMED);
        long shippedOrders = orderRepository.countByStatus(Order.OrderStatus.SHIPPED);
        long deliveredOrders = orderRepository.countByStatus(Order.OrderStatus.DELIVERED);
        long cancelledOrders = orderRepository.countByStatus(Order.OrderStatus.CANCELLED);
        
        return new OrderStatistics(totalOrders, pendingOrders, confirmedOrders, 
                                 shippedOrders, deliveredOrders, cancelledOrders);
    }
    
    /**
     * Order Item Request DTO
     */
    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;
        
        public OrderItemRequest() {}
        
        public OrderItemRequest(Long productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
        
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
    
    /**
     * Order Statistics DTO
     */
    public static class OrderStatistics {
        private final long totalOrders;
        private final long pendingOrders;
        private final long confirmedOrders;
        private final long shippedOrders;
        private final long deliveredOrders;
        private final long cancelledOrders;
        
        public OrderStatistics(long totalOrders, long pendingOrders, long confirmedOrders,
                             long shippedOrders, long deliveredOrders, long cancelledOrders) {
            this.totalOrders = totalOrders;
            this.pendingOrders = pendingOrders;
            this.confirmedOrders = confirmedOrders;
            this.shippedOrders = shippedOrders;
            this.deliveredOrders = deliveredOrders;
            this.cancelledOrders = cancelledOrders;
        }
        
        public long getTotalOrders() { return totalOrders; }
        public long getPendingOrders() { return pendingOrders; }
        public long getConfirmedOrders() { return confirmedOrders; }
        public long getShippedOrders() { return shippedOrders; }
        public long getDeliveredOrders() { return deliveredOrders; }
        public long getCancelledOrders() { return cancelledOrders; }
    }
}

