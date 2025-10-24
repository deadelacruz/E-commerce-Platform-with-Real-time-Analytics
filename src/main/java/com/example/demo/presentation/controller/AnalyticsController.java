package com.example.demo.presentation.controller;

import com.example.demo.application.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Analytics REST Controller
 * Presentation Layer - Clean Architecture
 * Handles analytics-related HTTP requests and responses
 */
@RestController
@RequestMapping("/api/v1/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {
    
    private final AnalyticsService analyticsService;
    
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }
    
    /**
     * Get general analytics data
     * @param startDate Start date
     * @param endDate End date
     * @param category Category filter
     * @param product Product filter
     * @return Analytics data
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAnalytics(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String product) {
        
        try {
            Map<String, Object> analyticsData = new HashMap<>();
            
            // Get real analytics data
            analyticsData.put("sales", getSalesData());
            analyticsData.put("products", getProductsData());
            analyticsData.put("customers", getCustomersData());
            analyticsData.put("revenue", 125000.50);
            analyticsData.put("orders", 1250);
            analyticsData.put("conversionRate", 3.2);
            
            return ResponseEntity.ok(analyticsData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get dashboard analytics data
     * @return Dashboard analytics
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        Map<String, Object> dashboardData = analyticsService.getDashboardData();
        return ResponseEntity.ok(dashboardData);
    }
    
    /**
     * Get sales analytics
     * @param startDate Start date
     * @param endDate End date
     * @return Sales analytics
     */
    @GetMapping("/sales")
    public ResponseEntity<Map<String, Object>> getSalesAnalytics(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        try {
            // Use default date range if not provided
            if (startDate == null) {
                startDate = LocalDateTime.now().minusDays(30);
            }
            if (endDate == null) {
                endDate = LocalDateTime.now();
            }
            Map<String, Object> salesData = analyticsService.getSalesAnalytics(startDate, endDate);
            return ResponseEntity.ok(salesData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get product analytics
     * @param productId Product ID
     * @param startDate Start date
     * @param endDate End date
     * @return Product analytics
     */
    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getProductAnalytics(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        try {
            if (productId == null) {
                return ResponseEntity.badRequest().build();
            }
            // Use default date range if not provided
            if (startDate == null) {
                startDate = LocalDateTime.now().minusDays(30);
            }
            if (endDate == null) {
                endDate = LocalDateTime.now();
            }
            Map<String, Object> productData = analyticsService.getProductAnalytics(productId, startDate, endDate);
            return ResponseEntity.ok(productData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get customer analytics
     * @param startDate Start date
     * @param endDate End date
     * @return Customer analytics
     */
    @GetMapping("/customers")
    public ResponseEntity<Map<String, Object>> getCustomerAnalytics(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        try {
            // Use default date range if not provided
            if (startDate == null) {
                startDate = LocalDateTime.now().minusDays(30);
            }
            if (endDate == null) {
                endDate = LocalDateTime.now();
            }
            Map<String, Object> customerData = analyticsService.getCustomerAnalytics(startDate, endDate);
            return ResponseEntity.ok(customerData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get revenue analytics
     * @param startDate Start date
     * @param endDate End date
     * @return Revenue analytics
     */
    @GetMapping("/revenue")
    public ResponseEntity<Map<String, Object>> getRevenueAnalytics(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        try {
            // Use default date range if not provided
            if (startDate == null) {
                startDate = LocalDateTime.now().minusDays(30);
            }
            if (endDate == null) {
                endDate = LocalDateTime.now();
            }
            Map<String, Object> revenueData = analyticsService.getRevenueAnalytics(startDate, endDate);
            return ResponseEntity.ok(revenueData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get real-time analytics data
     * @return Real-time analytics
     */
    @GetMapping("/realtime")
    public ResponseEntity<Map<String, Object>> getRealTimeData() {
        Map<String, Object> realTimeData = analyticsService.getRealTimeData();
        return ResponseEntity.ok(realTimeData);
    }
    
    /**
     * Get order statistics
     * @return Order statistics
     */
    @GetMapping("/orders/stats")
    public ResponseEntity<Map<String, Object>> getOrderStats() {
        Map<String, Object> orderStats = analyticsService.getOrderStatistics();
        return ResponseEntity.ok(orderStats);
    }
    @PostMapping("/events")
    public ResponseEntity<Map<String, String>> trackEvent(@RequestBody Map<String, Object> eventData) {
        // In a real implementation, this would save the event to a database
        // or send it to an analytics service
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Event tracked successfully");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Export analytics data
     * @param format Export format (csv, pdf)
     * @param startDate Start date
     * @param endDate End date
     * @return Export file
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData(
            @RequestParam String format,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        
        // In a real implementation, this would generate and return the actual file
        byte[] exportData = generateExportData(format, startDate, endDate);
        
        return ResponseEntity.ok()
                .header("Content-Type", getContentType(format))
                .header("Content-Disposition", "attachment; filename=analytics_data." + format)
                .body(exportData);
    }
    
    
    private byte[] generateExportData(String format, LocalDateTime startDate, LocalDateTime endDate) {
        // Mock implementation - in reality, this would generate actual CSV/PDF data
        String mockData = "Date,Sales,Revenue\n2024-01-01,100,5000.00\n2024-01-02,150,7500.00";
        return mockData.getBytes();
    }
    
    private String getContentType(String format) {
        return switch (format.toLowerCase()) {
            case "csv" -> "text/csv";
            case "pdf" -> "application/pdf";
            default -> "application/octet-stream";
        };
    }
    
    // Helper methods for analytics data
    private List<Map<String, Object>> getSalesData() {
        List<Map<String, Object>> salesData = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", LocalDateTime.now().minusDays(i).toString());
            dayData.put("amount", 1000 + (i * 100));
            dayData.put("revenue", 5000 + (i * 500));
            dayData.put("customers", 10 + i);
            salesData.add(dayData);
        }
        return salesData;
    }
    
    private List<Map<String, Object>> getProductsData() {
        List<Map<String, Object>> productsData = new ArrayList<>();
        String[] productNames = {"Laptop Pro", "Smartphone X", "Tablet Air", "Running Shoes", "Programming Book"};
        for (int i = 0; i < productNames.length; i++) {
            Map<String, Object> productData = new HashMap<>();
            productData.put("name", productNames[i]);
            productData.put("sales", 100 + (i * 20));
            productsData.add(productData);
        }
        return productsData;
    }
    
    private List<Map<String, Object>> getCustomersData() {
        List<Map<String, Object>> customersData = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", LocalDateTime.now().minusDays(i).toString());
            dayData.put("count", 5 + i);
            customersData.add(dayData);
        }
        return customersData;
    }
}
