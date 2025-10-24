package com.example.demo.application.service;

import com.example.demo.domain.entity.Order;
import com.example.demo.domain.entity.Product;
import com.example.demo.domain.repository.OrderRepository;
import com.example.demo.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Analytics Application Service
 * Application Layer - Service Layer
 * Handles analytics and reporting operations
 */
@Service
@Transactional(readOnly = true)
public class AnalyticsService {
    
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    
    public AnalyticsService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }
    
    /**
     * Get dashboard analytics data
     * @return Dashboard analytics
     */
    public Map<String, Object> getDashboardData() {
        Map<String, Object> dashboardData = new HashMap<>();
        
        try {
            // Calculate total revenue
            List<Order> allOrders = orderRepository.findAll();
            BigDecimal totalRevenue = allOrders.stream()
                    .map(Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // Calculate total orders
            long totalOrders = orderRepository.count();
            
            // Calculate conversion rate (mock data for now)
            double conversionRate = 3.2;
            
            // Calculate active users (mock data for now)
            long activeUsers = 450;
            
            dashboardData.put("totalRevenue", totalRevenue);
            dashboardData.put("totalOrders", totalOrders);
            dashboardData.put("conversionRate", conversionRate);
            dashboardData.put("activeUsers", activeUsers);
            dashboardData.put("topProducts", getTopProducts());
            dashboardData.put("recentOrders", getRecentOrders());
        } catch (Exception e) {
            // Return default values on error
            dashboardData.put("totalRevenue", BigDecimal.ZERO);
            dashboardData.put("totalOrders", 0L);
            dashboardData.put("conversionRate", 0.0);
            dashboardData.put("activeUsers", 0L);
            dashboardData.put("topProducts", new HashMap<>());
            dashboardData.put("recentOrders", new HashMap<>());
        }
        
        return dashboardData;
    }
    
    /**
     * Get sales analytics for a date range
     * @param startDate Start date
     * @param endDate End date
     * @return Sales analytics
     */
    public Map<String, Object> getSalesAnalytics(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> salesData = new HashMap<>();
        
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        
        List<Order> ordersInRange = orderRepository.findByDateRange(startDate, endDate);
        
        BigDecimal totalSales = ordersInRange.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        double averageOrderValue = ordersInRange.isEmpty() ? 0.0 : 
                totalSales.divide(BigDecimal.valueOf(ordersInRange.size()), 2, java.math.RoundingMode.HALF_UP).doubleValue();
        
        // Calculate sales growth (mock data for now)
        double salesGrowth = 12.5;
        
        salesData.put("totalSales", totalSales);
        salesData.put("salesGrowth", salesGrowth);
        salesData.put("averageOrderValue", averageOrderValue);
        salesData.put("salesByDay", getSalesByDay(ordersInRange));
        
        return salesData;
    }
    
    /**
     * Get product analytics
     * @param productId Product ID
     * @param startDate Start date
     * @param endDate End date
     * @return Product analytics
     */
    public Map<String, Object> getProductAnalytics(Long productId, LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> productData = new HashMap<>();
        
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        
        // Get product information
        var productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found: " + productId);
        }
        
        Product product = productOpt.get();
        
        // Calculate product sales (mock data for now)
        int totalSales = 1250;
        BigDecimal revenue = product.getPrice().multiply(BigDecimal.valueOf(totalSales));
        int views = 5000;
        double conversionRate = 25.0;
        
        productData.put("productId", productId);
        productData.put("productName", product.getName());
        productData.put("totalSales", totalSales);
        productData.put("revenue", revenue);
        productData.put("views", views);
        productData.put("conversionRate", conversionRate);
        
        return productData;
    }
    
    /**
     * Get customer analytics
     * @param startDate Start date
     * @param endDate End date
     * @return Customer analytics
     */
    public Map<String, Object> getCustomerAnalytics(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> customerData = new HashMap<>();
        
        List<Order> ordersInRange = orderRepository.findByDateRange(startDate, endDate);
        
        // Calculate unique customers
        long uniqueCustomers = ordersInRange.stream()
                .map(Order::getCustomerId)
                .distinct()
                .count();
        
        // Mock data for customer analytics
        long newCustomers = 150;
        long returningCustomers = 300;
        double customerRetention = 75.5;
        double averageCustomerValue = 250.75;
        
        customerData.put("totalCustomers", uniqueCustomers);
        customerData.put("newCustomers", newCustomers);
        customerData.put("returningCustomers", returningCustomers);
        customerData.put("customerRetention", customerRetention);
        customerData.put("averageCustomerValue", averageCustomerValue);
        
        return customerData;
    }
    
    /**
     * Get revenue analytics
     * @param startDate Start date
     * @param endDate End date
     * @return Revenue analytics
     */
    public Map<String, Object> getRevenueAnalytics(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> revenueData = new HashMap<>();
        
        List<Order> ordersInRange = orderRepository.findByDateRange(startDate, endDate);
        
        BigDecimal totalRevenue = ordersInRange.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Mock data for revenue analytics
        double revenueGrowth = 18.5;
        
        revenueData.put("totalRevenue", totalRevenue);
        revenueData.put("revenueGrowth", revenueGrowth);
        revenueData.put("revenueByChannel", getRevenueByChannel());
        revenueData.put("revenueByProduct", getRevenueByProduct());
        
        return revenueData;
    }
    
    /**
     * Get real-time analytics data
     * @return Real-time analytics
     */
    public Map<String, Object> getRealTimeData() {
        Map<String, Object> realTimeData = new HashMap<>();
        
        // Mock real-time data
        long activeUsers = 45;
        BigDecimal currentSales = BigDecimal.valueOf(1250.75);
        
        realTimeData.put("activeUsers", activeUsers);
        realTimeData.put("currentSales", currentSales);
        realTimeData.put("topProducts", getTopProducts());
        realTimeData.put("recentOrders", getRecentOrders());
        
        return realTimeData;
    }
    
    /**
     * Get order statistics
     * @return Order statistics
     */
    public Map<String, Object> getOrderStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalOrders", orderRepository.count());
        stats.put("pendingOrders", orderRepository.countByStatus(Order.OrderStatus.PENDING));
        stats.put("confirmedOrders", orderRepository.countByStatus(Order.OrderStatus.CONFIRMED));
        stats.put("shippedOrders", orderRepository.countByStatus(Order.OrderStatus.SHIPPED));
        stats.put("deliveredOrders", orderRepository.countByStatus(Order.OrderStatus.DELIVERED));
        stats.put("cancelledOrders", orderRepository.countByStatus(Order.OrderStatus.CANCELLED));
        
        return stats;
    }
    
    // Helper methods
    private Map<String, Object> getTopProducts() {
        Map<String, Object> topProducts = new HashMap<>();
        
        try {
            List<Product> products = productRepository.findByIsActiveTrue();
            if (products == null || products.isEmpty()) {
                topProducts.put("products", new ArrayList<>());
                return topProducts;
            }
            
            List<Map<String, Object>> productList = products.stream()
                    .limit(3)
                    .map(p -> {
                        Map<String, Object> productInfo = new HashMap<>();
                        productInfo.put("name", p.getName());
                        productInfo.put("sales", p.getStockQuantity() / 2); // Mock sales data
                        productInfo.put("revenue", p.getPrice().multiply(BigDecimal.valueOf(p.getStockQuantity() / 2)));
                        return productInfo;
                    })
                    .toList();
            
            topProducts.put("products", productList);
        } catch (Exception e) {
            topProducts.put("products", new ArrayList<>());
        }
        
        return topProducts;
    }
    
    private Map<String, Object> getRecentOrders() {
        Map<String, Object> recentOrders = new HashMap<>();
        
        List<Order> orders = orderRepository.findRecentOrders(LocalDateTime.now().minusDays(7));
        List<Map<String, Object>> orderList = orders.stream()
                .limit(5)
                .map(o -> {
                    Map<String, Object> orderInfo = new HashMap<>();
                    orderInfo.put("id", o.getOrderNumber());
                    orderInfo.put("customer", "Customer " + o.getCustomerId());
                    orderInfo.put("amount", o.getTotalAmount());
                    orderInfo.put("status", o.getStatus().toString());
                    orderInfo.put("time", o.getCreatedAt());
                    return orderInfo;
                })
                .toList();
        
        recentOrders.put("orders", orderList);
        return recentOrders;
    }
    
    private Map<String, Object> getSalesByDay(List<Order> orders) {
        Map<String, Object> salesByDay = new HashMap<>();
        
        // Group orders by day and calculate sales
        Map<String, BigDecimal> dailySales = new HashMap<>();
        for (Order order : orders) {
            String date = order.getCreatedAt().toLocalDate().toString();
            dailySales.merge(date, order.getTotalAmount(), BigDecimal::add);
        }
        
        List<Map<String, Object>> dailyData = dailySales.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> dayData = new HashMap<>();
                    dayData.put("date", entry.getKey());
                    dayData.put("sales", entry.getValue());
                    return dayData;
                })
                .toList();
        
        salesByDay.put("data", dailyData);
        return salesByDay;
    }
    
    private Map<String, Object> getRevenueByChannel() {
        Map<String, Object> revenueByChannel = new HashMap<>();
        revenueByChannel.put("online", BigDecimal.valueOf(45000.00));
        revenueByChannel.put("mobile", BigDecimal.valueOf(20000.00));
        revenueByChannel.put("inStore", BigDecimal.valueOf(10000.00));
        return revenueByChannel;
    }
    
    private Map<String, Object> getRevenueByProduct() {
        Map<String, Object> revenueByProduct = new HashMap<>();
        
        List<Product> products = productRepository.findByIsActiveTrue();
        for (Product product : products) {
            BigDecimal revenue = product.getPrice().multiply(BigDecimal.valueOf(product.getStockQuantity() / 2));
            revenueByProduct.put(product.getCategory().toLowerCase(), revenue);
        }
        
        return revenueByProduct;
    }
}