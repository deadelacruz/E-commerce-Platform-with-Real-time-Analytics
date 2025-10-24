package com.example.demo.infrastructure.config;

import com.example.demo.domain.entity.Product;
import com.example.demo.domain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Data Initialization Component
 * Infrastructure Layer - Configuration
 * Populates the database with sample data on startup
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        // Clear existing products and reinitialize to ensure image URLs are updated
        productRepository.deleteAll();
        initializeSampleProducts();
    }

    private void initializeSampleProducts() {
        // Create sample products
        Product laptop = new Product(
            "Laptop Pro",
            "High-performance laptop for professionals",
            new BigDecimal("1299.99"),
            50,
            "Electronics"
        );
        laptop.setImageUrl("/images/products/laptop-pro.jpg");
        productRepository.save(laptop);

        Product smartphone = new Product(
            "Smartphone X",
            "Latest smartphone with advanced features",
            new BigDecimal("899.99"),
            100,
            "Electronics"
        );
        smartphone.setImageUrl("/images/products/smartphone-x.svg");
        productRepository.save(smartphone);

        Product tablet = new Product(
            "Tablet Air",
            "Lightweight tablet for productivity",
            new BigDecimal("599.99"),
            75,
            "Electronics"
        );
        tablet.setImageUrl("/images/products/tablet-air.svg");
        productRepository.save(tablet);

        Product shoes = new Product(
            "Running Shoes",
            "Comfortable running shoes for athletes",
            new BigDecimal("129.99"),
            200,
            "Sports"
        );
        shoes.setImageUrl("/images/products/running-shoes.svg");
        productRepository.save(shoes);

        Product book = new Product(
            "Programming Book",
            "Complete guide to modern programming",
            new BigDecimal("49.99"),
            150,
            "Books"
        );
        book.setImageUrl("/images/products/programming-book.svg");
        productRepository.save(book);

        // Add more sample products
        Product headphones = new Product(
            "Wireless Headphones",
            "Premium noise-canceling wireless headphones",
            new BigDecimal("299.99"),
            80,
            "Electronics"
        );
        headphones.setImageUrl("/images/products/wireless-headphones.svg");
        productRepository.save(headphones);

        Product jacket = new Product(
            "Winter Jacket",
            "Warm and stylish winter jacket",
            new BigDecimal("199.99"),
            60,
            "Clothing"
        );
        jacket.setImageUrl("/images/products/winter-jacket.svg");
        productRepository.save(jacket);

        Product watch = new Product(
            "Smart Watch",
            "Advanced fitness tracking smart watch",
            new BigDecimal("399.99"),
            40,
            "Electronics"
        );
        watch.setImageUrl("/images/products/smart-watch.svg");
        productRepository.save(watch);

        System.out.println("Sample products initialized successfully!");
    }
}
