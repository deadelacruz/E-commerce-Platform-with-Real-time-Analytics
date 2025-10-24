/**
 * E-commerce Platform Angular.js Application
 * Demonstrates advanced Angular.js capabilities:
 * - Complex routing with nested views
 * - Real-time data binding
 * - Custom directives
 * - Service architecture
 * - State management
 * - Performance optimization
 */

// Main Application Module
angular.module('ecommerceApp', [
    'ngRoute',
    'ngResource',
    'ngAnimate',
    'ngSanitize'
])

// Application Configuration
.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    
    $routeProvider
        .when('/', {
            redirectTo: '/products'
        })
        .when('/products', {
            templateUrl: 'templates/products.html',
            controller: 'ProductController'
        })
        .when('/analytics', {
            templateUrl: 'templates/analytics.html',
            controller: 'AnalyticsController'
        })
        
        .otherwise({
            redirectTo: '/'
        });
}])

// Application Run Block
.run(['$rootScope', 'AnalyticsService', function($rootScope, AnalyticsService) {
    // Initialize global services
    $rootScope.loading = false;
    $rootScope.notifications = [];
    
    // Initialize analytics (no chat/cart)
    AnalyticsService.initialize();
    
    // Global notification system
    $rootScope.addNotification = function(title, message, type = 'info') {
        $rootScope.notifications.push({
            title: title,
            message: message,
            type: type,
            timestamp: new Date()
        });
        
        // Auto-remove notification after 5 seconds
        setTimeout(function() {
            $rootScope.removeNotification($rootScope.notifications.length - 1);
        }, 5000);
    };
    
    $rootScope.removeNotification = function(index) {
        $rootScope.notifications.splice(index, 1);
    };
    
    // Global loading state management
    $rootScope.setLoading = function(loading) {
        $rootScope.loading = loading;
    };
}])

// No Home or Nav controllers needed after pruning chat/cart
; 
