/**
 * Analytics Service
 * Demonstrates Angular.js advanced service features:
 * - Real-time data streaming
 * - WebSocket communication
 * - Data aggregation
 * - Performance monitoring
 * - Caching strategies
 */

angular.module('ecommerceApp')
.service('AnalyticsService', ['$http', '$q', '$rootScope', '$interval', function($http, $q, $rootScope, $interval) {
    
    var self = this;
    var realTimeData = {};
    var metricsInterval = null;
    
    
    // Initialize analytics service
    this.initialize = function() {
        // Start metrics collection
        this.startMetricsCollection();
    };
    
    // Start collecting real-time metrics
    this.startMetricsCollection = function() {
        metricsInterval = $interval(function() {
            self.collectRealTimeMetrics();
        }, 5000); // Collect every 5 seconds
    };
    
    // Stop metrics collection
    this.stopMetricsCollection = function() {
        if (metricsInterval) {
            $interval.cancel(metricsInterval);
            metricsInterval = null;
        }
    };
    
    // Collect real-time metrics
    this.collectRealTimeMetrics = function() {
        var config = {
            method: 'GET',
            url: '/api/v1/analytics/realtime'
        };
        
        $http(config).then(function(response) {
            if (response && response.data) {
                realTimeData = response.data;
                $rootScope.$broadcast('metrics:updated', realTimeData);
            }
        }).catch(function(error) {
            console.error('Error collecting real-time metrics:', error);
            // Set default values on error
            realTimeData = {
                activeUsers: 0,
                currentSales: 0,
                topProducts: [],
                recentOrders: []
            };
        });
    };
    
    // Get analytics data with filters
    this.getAnalytics = function(params) {
        var config = {
            method: 'GET',
            url: '/api/v1/analytics',
            params: params,
            timeout: 10000 // 10 second timeout
        };
        
        return $http(config).then(function(response) {
            return response.data;
        }).catch(function(error) {
            console.error('Error loading analytics:', error);
            // Return default data structure to prevent UI errors
            return {
                sales: [],
                products: [],
                customers: [],
                revenue: 0,
                orders: 0,
                conversionRate: 0
            };
        });
    };
    
    // Get dashboard data
    this.getDashboardData = function() {
        var config = {
            method: 'GET',
            url: '/api/v1/analytics/dashboard'
        };
        
        return $http(config).then(function(response) {
            return response.data;
        });
    };
    
    // Get sales analytics
    this.getSalesAnalytics = function(dateRange) {
        var params = {
            startDate: dateRange.start,
            endDate: dateRange.end
        };
        
        var config = {
            method: 'GET',
            url: '/api/v1/analytics/sales',
            params: params
        };
        
        return $http(config).then(function(response) {
            return response.data;
        });
    };
    
    // Get product analytics
    this.getProductAnalytics = function(productId, dateRange) {
        var params = {
            productId: productId,
            startDate: dateRange.start,
            endDate: dateRange.end
        };
        
        var config = {
            method: 'GET',
            url: '/api/v1/analytics/products',
            params: params
        };
        
        return $http(config).then(function(response) {
            return response.data;
        });
    };
    
    // Get customer analytics
    this.getCustomerAnalytics = function(dateRange) {
        var params = {
            startDate: dateRange.start,
            endDate: dateRange.end
        };
        
        var config = {
            method: 'GET',
            url: '/api/v1/analytics/customers',
            params: params
        };
        
        return $http(config).then(function(response) {
            return response.data;
        });
    };
    
    // Get revenue analytics
    this.getRevenueAnalytics = function(dateRange) {
        var params = {
            startDate: dateRange.start,
            endDate: dateRange.end
        };
        
        var config = {
            method: 'GET',
            url: '/api/v1/analytics/revenue',
            params: params
        };
        
        return $http(config).then(function(response) {
            return response.data;
        });
    };
    
    // Track custom events
    this.trackEvent = function(eventName, eventData) {
        var config = {
            method: 'POST',
            url: '/api/v1/analytics/events',
            data: {
                event: eventName,
                data: eventData,
                timestamp: new Date(),
                userAgent: navigator.userAgent,
                url: window.location.href
            }
        };
        
        $http(config).catch(function(error) {
            console.error('Error tracking event:', error);
        });
    };
    
    // Get real-time data
    this.getRealTimeData = function() {
        return realTimeData;
    };
    
    // Export analytics data
    this.exportData = function(format, dateRange) {
        var params = {
            format: format,
            startDate: dateRange.start,
            endDate: dateRange.end
        };
        
        var config = {
            method: 'GET',
            url: '/api/v1/analytics/export',
            params: params,
            responseType: 'blob'
        };
        
        return $http(config).then(function(response) {
            var blob = new Blob([response.data], { type: response.headers('content-type') });
            var url = window.URL.createObjectURL(blob);
            var a = document.createElement('a');
            a.href = url;
            a.download = 'analytics_data.' + format;
            a.click();
            window.URL.revokeObjectURL(url);
        });
    };
    
    
    // Real-time event listeners
    this.onMetricsUpdate = function(callback) {
        $rootScope.$on('metrics:updated', function(event, metrics) {
            callback(metrics);
        });
    };
    
    // Performance monitoring
    this.getPerformanceMetrics = function() {
        return {
            realTimeConnections: 0,
            dataPointsCollected: Object.keys(realTimeData).length,
            averageResponseTime: 200, // Mock value
            errorRate: 0.02 // Mock value
        };
    };
    
    
    // Cleanup
    this.destroy = function() {
        this.stopMetricsCollection();
        // Clear any remaining intervals
        if (metricsInterval) {
            $interval.cancel(metricsInterval);
            metricsInterval = null;
        }
    };
}]);
