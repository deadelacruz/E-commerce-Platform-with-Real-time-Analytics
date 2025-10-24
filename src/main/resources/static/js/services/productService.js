/**
 * Product Service
 * Demonstrates Angular.js service architecture:
 * - HTTP communication
 * - Caching strategies
 * - Error handling
 * - Real-time updates
 * - Performance optimization
 */

angular.module('ecommerceApp')
.service('ProductService', ['$http', '$q', '$rootScope', function($http, $q, $rootScope) {
    
    var self = this;
    var cache = {};
    var cacheTimeout = 5 * 60 * 1000; // 5 minutes
    
    
    // Cache management
    this.getCacheKey = function(params) {
        return JSON.stringify(params);
    };
    
    this.isCacheValid = function(cacheKey) {
        if (!cache[cacheKey]) return false;
        return Date.now() - cache[cacheKey].timestamp < cacheTimeout;
    };
    
    this.setCache = function(cacheKey, data) {
        cache[cacheKey] = {
            data: data,
            timestamp: Date.now()
        };
    };
    
    this.getCache = function(cacheKey) {
        return cache[cacheKey] ? cache[cacheKey].data : null;
    };
    
    this.clearCache = function() {
        cache = {};
    };
    
    // HTTP request wrapper with error handling
    this.makeRequest = function(config) {
        // Add timeout to all requests
        config.timeout = config.timeout || 10000;
        
        return $http(config).then(function(response) {
            return response.data;
        }).catch(function(error) {
            console.error('HTTP Error:', error);
            // Return empty data structure to prevent UI errors
            if (config.url.includes('/products')) {
                return { content: [], totalElements: 0 };
            }
            return $q.reject(error);
        });
    };
    
    // Get products with advanced filtering and pagination
    this.getProducts = function(params) {
        var cacheKey = this.getCacheKey(params);
        
        if (this.isCacheValid(cacheKey)) {
            return $q.resolve(this.getCache(cacheKey));
        }
        
        var config = {
            method: 'GET',
            url: '/api/v1/products',
            params: params
        };
        
        return this.makeRequest(config).then(function(data) {
            self.setCache(cacheKey, data);
            return data;
        });
    };
    
    // Get single product by ID
    this.getProduct = function(id) {
        var cacheKey = 'product_' + id;
        
        if (this.isCacheValid(cacheKey)) {
            return $q.resolve(this.getCache(cacheKey));
        }
        
        var config = {
            method: 'GET',
            url: '/api/v1/products/' + id
        };
        
        return this.makeRequest(config).then(function(data) {
            self.setCache(cacheKey, data);
            return data;
        });
    };
    
    
    // Get product categories
    this.getCategories = function() {
        var cacheKey = 'categories';
        
        if (this.isCacheValid(cacheKey)) {
            return $q.resolve(this.getCache(cacheKey));
        }
        
        var config = {
            method: 'GET',
            url: '/api/v1/products/categories'
        };
        
        return this.makeRequest(config).then(function(categories) {
            self.setCache(cacheKey, categories);
            return categories;
        });
    };
    
    // Real-time event listeners
    this.onStockUpdate = function(callback) {
        $rootScope.$on('stock:updated', function(event, stockUpdate) {
            callback(stockUpdate);
        });
    };
    
}]);
