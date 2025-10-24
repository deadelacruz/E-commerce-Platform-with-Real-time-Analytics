/**
 * Product Controller
 * Demonstrates Angular.js advanced features:
 * - Complex data binding
 * - Custom filters
 * - Performance optimization with track by
 * - Real-time updates
 * - Advanced search and filtering
 */

angular.module('ecommerceApp')
.controller('ProductController', ['$scope', '$routeParams', 'ProductService', 'AnalyticsService', function($scope, $routeParams, ProductService, AnalyticsService) {
    
    // Controller state
    $scope.products = [];
    $scope.filteredProducts = [];
    $scope.categories = [];
    $scope.sortBy = 'name';
    $scope.sortOrder = 'asc';
    $scope.currentPage = 1;
    $scope.itemsPerPage = 12;
    $scope.totalItems = 0;
    $scope.loading = false;
    $scope.filtering = false;
    $scope.selectedQuantity = 1;
    
    // Dynamic UI state
    $scope.showProductDetail = false;
    $scope.selectedProduct = null;
    $scope.viewMode = 'grid'; // 'grid' or 'detail'
    
    // Unified search filters - ALL filtering variables in one place
    $scope.filters = {
        searchTerm: '',
        category: '',
        priceRange: { min: 0, max: 1000 },
        inStock: false,
        rating: 0
    };
    
    // Initialize controller
    $scope.init = function() {
        $scope.loadProducts();
        $scope.loadCategories();
        $scope.setupRealTimeUpdates();
        
        // Ensure initial filtering is applied
        setTimeout(function() {
            $scope.applyFilters();
        }, 1000);
    };
    
    // Load products with advanced filtering
    $scope.loadProducts = function() {
        $scope.loading = true;
        
        var params = {
            page: $scope.currentPage - 1,
            size: 100, // Load more products for better client-side filtering
            sort: $scope.sortBy + ',' + $scope.sortOrder
        };
        
        // For category filtering, we'll do client-side filtering
        // Only add server-side filters for search and other criteria
        if ($scope.filters.searchTerm) {
            params.search = $scope.filters.searchTerm;
        }
        // Don't send category filter to server - do it client-side
        if ($scope.filters.priceRange.min > 0) {
            params.minPrice = $scope.filters.priceRange.min;
        }
        if ($scope.filters.priceRange.max > 0 && $scope.filters.priceRange.max < 1000) {
            params.maxPrice = $scope.filters.priceRange.max;
        }
        if ($scope.filters.inStock) {
            params.inStock = true;
        }
        
        
        ProductService.getProducts(params).then(function(response) {
            $scope.products = response.content || response;
            $scope.totalItems = response.totalElements || response.length;
            $scope.loading = false;
            
            // Initialize filtered products with all products
            $scope.filteredProducts = $scope.products.slice();
            
            // Apply filters after loading
            $scope.applyFilters();
        }).catch(function(error) {
            console.error('Error loading products:', error);
            $scope.loading = false;
        });
    };
    
    // Load categories
    $scope.loadCategories = function() {
        console.log('Loading categories...');
        ProductService.getCategories().then(function(categories) {
            console.log('Categories loaded:', categories);
            $scope.categories = categories;
        }).catch(function(error) {
            console.error('Error loading categories:', error);
        });
    };
    
    // Advanced filtering system
    $scope.applyFilters = function() {
        console.log('Applying filters:', $scope.filters);
        console.log('Total products:', $scope.products.length);
        
        // Add loading animation
        $scope.filtering = true;
        
        // Use setTimeout to allow for smooth animation
        setTimeout(function() {
            if (!$scope.products || $scope.products.length === 0) {
                console.log('No products to filter');
                $scope.filteredProducts = [];
                $scope.filtering = false;
                if (!$scope.$$phase && !$scope.$root.$$phase) {
                    $scope.$apply();
                }
                return;
            }
            
            $scope.filteredProducts = $scope.products.filter(function(product) {
                var matches = true;
                
                // Search term filter
                if ($scope.filters.searchTerm && $scope.filters.searchTerm.trim() !== '') {
                    var searchLower = $scope.filters.searchTerm.toLowerCase();
                    matches = matches && (
                        product.name.toLowerCase().includes(searchLower) ||
                        product.description.toLowerCase().includes(searchLower)
                    );
                }
                
                // Category filter
                if ($scope.filters.category && $scope.filters.category !== '') {
                    console.log('Filtering by category:', $scope.filters.category, 'Product category:', product.category);
                    matches = matches && product.category === $scope.filters.category;
                }
                
                // Price range filter
                if ($scope.filters.priceRange.min > 0) {
                    matches = matches && product.price >= $scope.filters.priceRange.min;
                }
                if ($scope.filters.priceRange.max > 0 && $scope.filters.priceRange.max < 1000) {
                    matches = matches && product.price <= $scope.filters.priceRange.max;
                }
                
                // Stock filter
                if ($scope.filters.inStock) {
                    matches = matches && product.stockQuantity > 0;
                }
                
                return matches;
            });
            
            console.log('Filtered products count:', $scope.filteredProducts.length);
            $scope.filtering = false;
            if (!$scope.$$phase && !$scope.$root.$$phase) {
                $scope.$apply();
            }
        }, 100);
    };
    
    // Real-time search with debouncing
    $scope.searchTimeout = null;
    $scope.onSearchChange = function() {
        if ($scope.searchTimeout) {
            clearTimeout($scope.searchTimeout);
        }
        
        $scope.searchTimeout = setTimeout(function() {
            $scope.applyFilters();
        }, 300);
    };
    
    // Performance optimization: Throttle filter changes
    $scope.filterThrottleTimeout = null;
    $scope.throttledFilterChange = function() {
        if ($scope.filterThrottleTimeout) {
            clearTimeout($scope.filterThrottleTimeout);
        }
        
        $scope.filterThrottleTimeout = setTimeout(function() {
            $scope.onFilterChange();
        }, 100);
    };
    
    
    // Filter change handlers
    $scope.onFilterChange = function() {
        $scope.currentPage = 1;
        $scope.applyFilters();
    };
    
    // Clear all filters
    $scope.clearFilters = function() {
        $scope.filters = {
            searchTerm: '',
            category: '',
            priceRange: { min: 0, max: 1000 },
            inStock: false,
            rating: 0
        };
        $scope.currentPage = 1;
        $scope.applyFilters();
    };
    
    // Handle category click
    $scope.selectCategory = function(category) {
        console.log('=== CATEGORY CLICKED ===');
        console.log('Category selected:', category);
        console.log('Current filters before:', $scope.filters);
        
        $scope.filters.category = category;
        $scope.currentPage = 1;
        
        console.log('Current filters after:', $scope.filters);
        
        // Apply filters immediately without server reload
        $scope.applyFilters();
        
        // Visual feedback removed - no notifications needed
        
        // Track analytics for category selection (with error handling)
        try {
            var categoryName = category || 'All Categories';
            AnalyticsService.trackEvent('category_selected', {
                category: categoryName,
                productCount: $scope.filteredProducts.length
            });
        } catch (error) {
            console.log('Analytics error (non-critical):', error);
        }
        
        console.log('Filtered products count:', $scope.filteredProducts.length);
        console.log('=== END CATEGORY CLICK ===');
    };
    
    // Sorting
    $scope.sort = function(field) {
        if ($scope.sortBy === field) {
            $scope.sortOrder = $scope.sortOrder === 'asc' ? 'desc' : 'asc';
        } else {
            $scope.sortBy = field;
            $scope.sortOrder = 'asc';
        }
        $scope.loadProducts();
    };
    
    // Pagination
    $scope.setPage = function(page) {
        $scope.currentPage = page;
        $scope.loadProducts();
    };
    
    $scope.getPageNumbers = function() {
        var pages = [];
        var totalPages = Math.ceil($scope.totalItems / $scope.itemsPerPage);
        var startPage = Math.max(1, $scope.currentPage - 2);
        var endPage = Math.min(totalPages, $scope.currentPage + 2);
        
        for (var i = startPage; i <= endPage; i++) {
            pages.push(i);
        }
        return pages;
    };
    
    // Add to cart with animation
    $scope.addToCart = function(product, quantity = 1) {
        // Show success animation
        $scope.showAddToCartAnimation(product);
        
        // Track analytics
        AnalyticsService.trackEvent('product_added_to_cart', {
            productId: product.id,
            productName: product.name,
            price: product.price
        });
    };
    
    // Show add to cart animation
    $scope.showAddToCartAnimation = function(product) {
        // Simple animation feedback - could be enhanced with CSS animations
        console.log('Added to cart:', product.name);
    };
    
    
    // Show product detail view
    $scope.showProductDetailView = function(product) {
        $scope.selectedProduct = product;
        $scope.showProductDetail = true;
        $scope.viewMode = 'detail';
        
        // Track analytics
        AnalyticsService.trackEvent('product_detail_viewed', {
            productId: product.id,
            productName: product.name,
            category: product.category
        });
    };
    
    // Back to grid view
    $scope.backToGrid = function() {
        $scope.showProductDetail = false;
        $scope.selectedProduct = null;
        $scope.viewMode = 'grid';
    };
    
    // Get related products (same category)
    $scope.getRelatedProducts = function() {
        if (!$scope.selectedProduct) return [];
        return $scope.products.filter(function(product) {
            return product.category === $scope.selectedProduct.category && 
                   product.id !== $scope.selectedProduct.id;
        }).slice(0, 4); // Show max 4 related products
    };
    
    // Real-time updates setup
    $scope.setupRealTimeUpdates = function() {
        // Listen for stock updates
        ProductService.onStockUpdate(function(stockUpdate) {
            if (stockUpdate && stockUpdate.productId && stockUpdate.newStock !== undefined) {
                var product = $scope.products.find(function(p) {
                    return p.id === stockUpdate.productId;
                });
                
                if (product) {
                    // Force proper update of stock quantity
                    product.stockQuantity = stockUpdate.newStock;
                    
                    // Safe DOM update - check if digest is not in progress
                    if (!$scope.$$phase && !$scope.$root.$$phase) {
                        $scope.$apply();
                    }
                    
                    // Force badge update to prevent overlap
                    $scope.forceBadgeUpdate();
                    
                    // Additional force update for badge classes
                    setTimeout(function() {
                        if (!$scope.$$phase && !$scope.$root.$$phase) {
                            $scope.$apply();
                        }
                        $scope.forceBadgeUpdate();
                    }, 100);
                }
            }
        });
    };
    
    // Get category count for display
    $scope.getCategoryCount = function(category) {
        if (!$scope.products) return 0;
        return $scope.products.filter(function(product) {
            return product.category === category;
        }).length;
    };
    
    
    // Get stock badge class based on quantity
    $scope.getStockBadgeClass = function(stockQuantity) {
        if (stockQuantity === 0 || stockQuantity === '0') {
            return 'bg-danger';
        } else if (stockQuantity < 10) {
            return 'bg-warning';
        } else {
            return 'bg-success';
        }
    };
    
    // Get stock badge text based on quantity
    $scope.getStockBadgeText = function(stockQuantity) {
        if (stockQuantity === 0 || stockQuantity === '0') {
            return 'Out of Stock';
        } else if (stockQuantity < 10) {
            return 'Low Stock';
        } else {
            return 'In Stock';
        }
    };
    
    // Force badge update to prevent overlap
    $scope.forceBadgeUpdate = function() {
        // Safe DOM update for stock badges
        if (!$scope.$$phase && !$scope.$root.$$phase) {
            $scope.$apply();
        }
    };
    
    
    // Cleanup on controller destroy
    $scope.$on('$destroy', function() {
        // Clear all timeouts
        if ($scope.searchTimeout) {
            clearTimeout($scope.searchTimeout);
        }
        if ($scope.filterThrottleTimeout) {
            clearTimeout($scope.filterThrottleTimeout);
        }
    });
    
    // Initialize controller
    $scope.init();
}]);
