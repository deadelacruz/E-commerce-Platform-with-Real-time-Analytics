package com.example.demo.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Database Configuration
 * Infrastructure Layer - Configuration
 * Enables JPA repositories and transaction management
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.example.demo.domain.repository")
@EnableTransactionManagement
public class DatabaseConfig {
    // JPA repositories are automatically configured by Spring Boot
}
