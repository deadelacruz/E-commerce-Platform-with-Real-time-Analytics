/**
 * Analytics Controller
 * Demonstrates Angular.js advanced features:
 * - Real-time data visualization
 * - Complex chart integration
 * - WebSocket communication
 * - Performance monitoring
 * - Advanced filtering and aggregation
 */

angular.module('ecommerceApp')
.controller('AnalyticsController', ['$scope', '$interval', 'AnalyticsService', 'ProductService', function($scope, $interval, AnalyticsService, ProductService) {
    
    // Controller state
    $scope.analytics = {
        sales: [],
        products: [],
        customers: [],
        revenue: 0,
        orders: 0,
        conversionRate: 0
    };
    
    $scope.charts = {
        salesChart: null,
        productChart: null,
        revenueChart: null,
        customerChart: null
    };
    
    $scope.filters = {
        dateRange: {
            start: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000), // 30 days ago
            end: new Date()
        },
        category: '',
        product: ''
    };
    
    $scope.realTimeData = {
        activeUsers: 0,
        currentSales: 0,
        topProducts: [],
        recentOrders: []
    };
    
    $scope.loading = false;
    $scope.refreshInterval = null;
    
    // Initialize controller
    $scope.init = function() {
        $scope.loadAnalytics();
        $scope.setupRealTimeUpdates();
        $scope.startAutoRefresh();
    };
    
    // Load analytics data
    $scope.loadAnalytics = function() {
        $scope.loading = true;
        
        var params = {
            startDate: $scope.filters.dateRange.start,
            endDate: $scope.filters.dateRange.end,
            category: $scope.filters.category,
            product: $scope.filters.product
        };
        
        AnalyticsService.getAnalytics(params).then(function(data) {
            $scope.analytics = data;
            $scope.updateCharts();
            $scope.loading = false;
        }).catch(function(error) {
            console.error('Error loading analytics:', error);
            $scope.loading = false;
        });
    };
    
    // Update all charts
    $scope.updateCharts = function() {
        if ($scope.analytics && $scope.analytics.sales) {
            $scope.updateSalesChart();
        }
        if ($scope.analytics && $scope.analytics.products) {
            $scope.updateProductChart();
        }
        if ($scope.analytics && $scope.analytics.revenue) {
            $scope.updateRevenueChart();
        }
        if ($scope.analytics && $scope.analytics.customers) {
            $scope.updateCustomerChart();
        }
    };
    
    // Sales Chart - Line Chart
    $scope.updateSalesChart = function() {
        var ctx = document.getElementById('salesChart');
        if (!ctx || !$scope.analytics || !$scope.analytics.sales || $scope.analytics.sales.length === 0) return;
        
        if ($scope.charts.salesChart) {
            $scope.charts.salesChart.destroy();
        }
        
        try {
            $scope.charts.salesChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: $scope.analytics.sales.map(function(item) {
                    return new Date(item.date).toLocaleDateString();
                }),
                datasets: [{
                    label: 'Sales',
                    data: $scope.analytics.sales.map(function(item) {
                        return item.amount;
                    }),
                    borderColor: 'rgb(75, 192, 192)',
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    tension: 0.1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        text: 'Sales Over Time'
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
        } catch (error) {
            console.error('Error creating sales chart:', error);
        }
    };
    
    // Product Performance Chart - Bar Chart
    $scope.updateProductChart = function() {
        var ctx = document.getElementById('productChart');
        if (!ctx || !$scope.analytics || !$scope.analytics.products || $scope.analytics.products.length === 0) return;
        
        if ($scope.charts.productChart) {
            $scope.charts.productChart.destroy();
        }
        
        try {
            $scope.charts.productChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: $scope.analytics.products.map(function(item) {
                    return item.name;
                }),
                datasets: [{
                    label: 'Sales',
                    data: $scope.analytics.products.map(function(item) {
                        return item.sales;
                    }),
                    backgroundColor: 'rgba(54, 162, 235, 0.2)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        text: 'Top Performing Products'
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
        } catch (error) {
            console.error('Error creating product chart:', error);
        }
    };
    
    // Revenue Chart - Doughnut Chart
    $scope.updateRevenueChart = function() {
        var ctx = document.getElementById('revenueChart');
        if (!ctx) return;
        
        if ($scope.charts.revenueChart) {
            $scope.charts.revenueChart.destroy();
        }
        
        $scope.charts.revenueChart = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ['Online Sales', 'In-Store Sales', 'Mobile Sales'],
                datasets: [{
                    data: [
                        $scope.analytics.revenue * 0.6,
                        $scope.analytics.revenue * 0.3,
                        $scope.analytics.revenue * 0.1
                    ],
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.8)',
                        'rgba(54, 162, 235, 0.8)',
                        'rgba(255, 205, 86, 0.8)'
                    ]
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        text: 'Revenue Distribution'
                    }
                }
            }
        });
    };
    
    // Customer Chart - Area Chart
    $scope.updateCustomerChart = function() {
        var ctx = document.getElementById('customerChart');
        if (!ctx) return;
        
        if ($scope.charts.customerChart) {
            $scope.charts.customerChart.destroy();
        }
        
        $scope.charts.customerChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: $scope.analytics.customers.map(function(item) {
                    return new Date(item.date).toLocaleDateString();
                }),
                datasets: [{
                    label: 'New Customers',
                    data: $scope.analytics.customers.map(function(item) {
                        return item.count;
                    }),
                    borderColor: 'rgb(255, 99, 132)',
                    backgroundColor: 'rgba(255, 99, 132, 0.2)',
                    fill: true,
                    tension: 0.1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        text: 'Customer Growth'
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    };
    
    // Real-time updates setup
    $scope.setupRealTimeUpdates = function() {
        // Listen for real-time metrics
        AnalyticsService.onMetricsUpdate(function(metrics) {
            if (metrics && typeof metrics === 'object') {
                $scope.realTimeData = metrics;
                if (!$scope.$$phase && !$scope.$root.$$phase) {
                    $scope.$apply();
                }
            }
        });
    };
    
    // Auto-refresh functionality
    $scope.startAutoRefresh = function() {
        $scope.refreshInterval = $interval(function() {
            $scope.loadAnalytics();
        }, 30000); // Refresh every 30 seconds
    };
    
    $scope.stopAutoRefresh = function() {
        if ($scope.refreshInterval) {
            $interval.cancel($scope.refreshInterval);
            $scope.refreshInterval = null;
        }
    };
    
    // Toggle auto-refresh
    $scope.autoRefresh = true;
    $scope.toggleAutoRefresh = function() {
        if ($scope.autoRefresh) {
            $scope.startAutoRefresh();
        } else {
            $scope.stopAutoRefresh();
        }
    };
    
    // Filter change handlers
    $scope.onFilterChange = function() {
        $scope.loadAnalytics();
    };
    
    // Export analytics data
    $scope.exportData = function(format) {
        if (format === 'csv') {
            $scope.exportToCSV();
        } else if (format === 'pdf') {
            $scope.exportToPDF();
        }
    };
    
    $scope.exportToCSV = function() {
        var csvContent = "Date,Sales,Revenue,Customers\n";
        $scope.analytics.sales.forEach(function(item) {
            csvContent += item.date + "," + item.amount + "," + item.revenue + "," + item.customers + "\n";
        });
        
        var blob = new Blob([csvContent], { type: 'text/csv' });
        var url = window.URL.createObjectURL(blob);
        var a = document.createElement('a');
        a.href = url;
        a.download = 'analytics_data.csv';
        a.click();
    };
    
    $scope.exportToPDF = function() {
        // This would typically use a PDF generation library
        window.print();
    };
    
    
    // Cleanup on controller destroy
    $scope.$on('$destroy', function() {
        $scope.stopAutoRefresh();
        
        // Clear all timeouts
        if ($scope.searchTimeout) {
            clearTimeout($scope.searchTimeout);
        }
        if ($scope.filterThrottleTimeout) {
            clearTimeout($scope.filterThrottleTimeout);
        }
        
        // Destroy all charts
        Object.keys($scope.charts).forEach(function(key) {
            if ($scope.charts[key]) {
                $scope.charts[key].destroy();
            }
        });
    });
    
    // Initialize controller
    $scope.init();
}]);
