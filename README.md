# 🛒 E-commerce Platform with Real-time Analytics

A comprehensive e-commerce platform built with **Java 25**, **Spring Boot 3.3.5**, **Gradle 9.1.0**, and **Angular.js 1.8** that demonstrates clean architecture principles, SOLID design patterns, and modern web development practices.

## 🏗️ Architecture Overview

This application implements a **monolithic architecture** with **Clean Architecture** principles, featuring a well-structured Spring Boot backend and Angular.js frontend with real-time analytics capabilities.

### 🎯 Key Features

1. **Product Management** - Complete CRUD operations with advanced search and filtering
2. **Order Processing** - Full order lifecycle management with status tracking
3. **Real-time Analytics** - Live data visualization with Chart.js integration
4. **Clean Architecture** - Domain-driven design with separation of concerns
5. **RESTful APIs** - Well-designed REST endpoints with proper HTTP status codes
6. **Responsive UI** - Bootstrap-based responsive design

## 🏛️ Clean Architecture Implementation

```
src/main/java/com/example/demo/
├── domain/                    # Domain Layer (Business Logic)
│   ├── entity/               # Domain Entities
│   │   ├── Product.java      # Product entity with business logic
│   │   ├── Order.java        # Order entity with status management
│   │   └── OrderItem.java    # Order item entity
│   ├── repository/           # Repository Interfaces
│   │   ├── ProductRepository.java
│   │   └── OrderRepository.java
│   └── service/              # Domain Services
│       └── ProductDomainService.java
├── application/               # Application Layer (Use Cases)
│   ├── service/              # Application Services
│   │   ├── ProductService.java
│   │   ├── OrderService.java
│   │   └── AnalyticsService.java
│   └── usecase/              # Use Cases
│       └── product/
│           └── CreateProductUseCase.java
├── infrastructure/           # Infrastructure Layer
│   ├── config/               # Configuration Classes
│   │   ├── DatabaseConfig.java
│   │   ├── SecurityConfig.java
│   │   └── WebConfig.java
│   └── repository/           # JPA Implementations
└── presentation/             # Presentation Layer
    └── controller/           # REST Controllers
        ├── ProductController.java
        ├── OrderController.java
        └── AnalyticsController.java
```

## 🎨 Frontend Architecture (Angular.js)

```
src/main/resources/static/
├── index.html                 # Main application entry point
├── css/
│   └── app.css               # Custom styles
├── js/
│   ├── app.js                # Main Angular.js module
│   ├── controllers/          # Angular.js Controllers
│   │   ├── productController.js
│   │   └── analyticsController.js
│   └── services/             # Angular.js Services
│       ├── productService.js
│       └── analyticsService.js
└── templates/                # HTML Templates
    ├── products.html
    └── analytics.html
```

## 🛠️ Technology Stack

### Backend
- **Java 25** - Latest Java features and performance improvements
- **Spring Boot 3.3.5** - Enterprise-grade application framework
- **Spring Data JPA** - Data persistence with Hibernate
- **Spring Security** - Authentication and authorization
- **H2 Database** - In-memory database for development
- **Spring Actuator** - Application monitoring and health checks

### Frontend
- **Angular.js 1.8** - Single Page Application framework
- **Bootstrap 5** - Responsive UI framework
- **Chart.js** - Data visualization library
- **jQuery** - DOM manipulation and AJAX requests

### Development Tools
- **Gradle 9.1.0** - Build automation and dependency management
- **Spring Boot DevTools** - Development productivity tools
- **H2 Console** - Database management interface

## 🚀 Getting Started

### Prerequisites
- **Java 25** (recommended) or Java 21+
- **Gradle 9.1.0** (included via wrapper)

### Quick Start

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd "E commerce Platform with Real time Analytics"
   ```

2. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

3. **Access the application**
   - **Frontend**: http://localhost:8081
   - **H2 Database Console**: http://localhost:8081/h2-console
   - **API Endpoints**: http://localhost:8081/api/v1/
   - **Health Check**: http://localhost:8081/actuator/health
   - **Metrics**: http://localhost:8081/actuator/metrics

### Database Credentials (H2 Console)
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: `password`

### Application Credentials
- **Username**: `admin`
- **Password**: `admin123`
- **Role**: `ADMIN`

## 📊 API Endpoints

### Products
- `GET /api/v1/products` - Get all products (paginated)
- `GET /api/v1/products/{id}` - Get product by ID
- `POST /api/v1/products` - Create new product
- `PUT /api/v1/products/{id}` - Update product
- `DELETE /api/v1/products/{id}` - Deactivate product
- `GET /api/v1/products/categories` - Get all categories

### Orders
- `GET /api/v1/orders` - Get all orders (paginated)
- `GET /api/v1/orders/{id}` - Get order by ID
- `POST /api/v1/orders` - Create new order
- `PUT /api/v1/orders/{id}/status` - Update order status
- `PUT /api/v1/orders/{id}/cancel` - Cancel order
- `GET /api/v1/orders/customer/{customerId}` - Get orders by customer
- `GET /api/v1/orders/stats` - Get order statistics

### Analytics
- `GET /api/v1/analytics` - Get general analytics data
- `GET /api/v1/analytics/dashboard` - Get dashboard analytics
- `GET /api/v1/analytics/sales` - Get sales analytics
- `GET /api/v1/analytics/products` - Get product analytics
- `GET /api/v1/analytics/customers` - Get customer analytics
- `GET /api/v1/analytics/revenue` - Get revenue analytics
- `GET /api/v1/analytics/realtime` - Get real-time analytics
- `GET /api/v1/analytics/orders/stats` - Get order statistics
- `POST /api/v1/analytics/events` - Track analytics events
- `GET /api/v1/analytics/export` - Export analytics data

## 🏗️ SOLID Principles Implementation

### Single Responsibility Principle (SRP)
- **Controllers**: Handle HTTP requests and responses only
- **Services**: Contain business logic only
- **Repositories**: Handle data access only
- **Entities**: Represent domain objects with business rules

### Open/Closed Principle (OCP)
- **Use Cases**: Extensible through new use case implementations
- **Services**: Open for extension through new service methods
- **Controllers**: Closed for modification, open for new endpoints

### Liskov Substitution Principle (LSP)
- **Repository Interfaces**: All implementations are substitutable
- **Service Interfaces**: Maintain consistent contracts
- **Entity Inheritance**: Proper polymorphic behavior

### Interface Segregation Principle (ISP)
- **Focused Interfaces**: Small, specific interfaces for each concern
- **Repository Segregation**: Separate interfaces for different data operations
- **Service Segregation**: Specific services for different business domains

### Dependency Inversion Principle (DIP)
- **Dependency Injection**: Spring's IoC container manages dependencies
- **Interface Dependencies**: Services depend on abstractions, not concretions
- **Configuration Classes**: Externalize configuration dependencies

## 🗄️ Database Design

### Entities
- **Product**: Product information with stock management
- **Order**: Order details with status tracking
- **OrderItem**: Individual items within orders

### Key Features
- **ACID Compliance**: Transactional integrity
- **Data Validation**: JSR-303 validation annotations
- **Audit Fields**: Created/updated timestamps
- **Soft Deletes**: Product deactivation instead of hard deletion

## 🎨 Frontend Features

### Angular.js Capabilities
1. **Routing System**: Single-page application navigation
2. **Data Binding**: Two-way data binding with models
3. **Service Architecture**: Modular service-based design
4. **Real-time Updates**: Live data refresh capabilities
5. **Data Visualization**: Chart.js integration for analytics
6. **Responsive Design**: Bootstrap-based mobile-friendly UI

### Key Components
- **Product Catalog**: Advanced search and filtering
- **Analytics Dashboard**: Real-time data visualization
- **Order Management**: Complete order lifecycle tracking
- **Responsive Layout**: Mobile-optimized interface

## 🔒 Security Features

- **Spring Security**: Basic authentication and authorization
- **CORS Configuration**: Cross-origin resource sharing setup
- **Input Validation**: JSR-303 validation on all inputs
- **SQL Injection Prevention**: JPA parameterized queries
- **XSS Protection**: Input sanitization and validation

## 🚀 Java 25 Features & Performance

### Java 25 Optimizations
- **Latest JVM Performance**: Enhanced garbage collection and JIT compilation
- **Native Access**: Optimized native library access with `--enable-native-access=ALL-UNNAMED`
- **Modern Language Features**: Latest Java language improvements and optimizations
- **Memory Management**: Improved memory allocation and garbage collection

### Gradle 9.1.0 Optimizations
- **Configuration Cache**: Faster Gradle startup and build times
- **Parallel Builds**: Multi-threaded compilation for faster builds
- **Build Caching**: Intelligent caching for incremental builds
- **Daemon Optimization**: Persistent Gradle daemon for better performance

### Backend Optimizations
- **Connection Pooling**: Efficient database connections
- **Lazy Loading**: JPA lazy loading for relationships
- **Caching**: Spring Cache abstraction
- **Async Processing**: Non-blocking operations where applicable

### Frontend Optimizations
- **Service Caching**: Client-side data caching
- **Real-time Updates**: Efficient data refresh
- **Responsive Images**: Optimized image loading
- **Chart.js Integration**: Efficient data visualization

## 🧪 Testing Strategy

### Backend Testing
- **Unit Tests**: Service and repository layer testing
- **Integration Tests**: Database and API integration
- **Controller Tests**: REST endpoint testing
- **Security Tests**: Authentication and authorization testing

### Frontend Testing
- **Unit Tests**: Angular.js service and controller testing
- **Integration Tests**: API integration testing
- **E2E Tests**: End-to-end user workflow testing

## 🚀 Deployment

### Development
```bash
# Run with Java 25 and Gradle 9.1.0
./gradlew bootRun

# Or with specific JVM arguments
./gradlew bootRun -Dorg.gradle.jvmargs="--enable-native-access=ALL-UNNAMED"
```

### Production Build
```bash
# Clean build with optimizations
./gradlew clean build

# Run the JAR with Java 25
java --enable-native-access=ALL-UNNAMED -jar build/libs/demo-0.0.1-SNAPSHOT.jar
```

### Performance Optimized Build
```bash
# Build with all optimizations enabled
./gradlew clean build --configuration-cache --parallel --build-cache

# Run with optimized JVM settings
java -Xmx2048m -XX:MaxMetaspaceSize=512m --enable-native-access=ALL-UNNAMED -jar build/libs/demo-0.0.1-SNAPSHOT.jar
```

## 📱 Mobile Responsiveness

- **Bootstrap Grid**: Responsive layout system
- **Mobile Navigation**: Touch-friendly interface
- **Optimized Performance**: Fast loading on mobile devices
- **Progressive Enhancement**: Works without JavaScript

## 🔧 Configuration

### Current Setup (Java 25 + Gradle 9.1.0)
- **Java Version**: 25 (with Java 21 source/target compatibility)
- **Gradle Version**: 9.1.0 with configuration cache and parallel builds
- **Spring Boot**: 3.3.5 with full Java 25 support
- **JVM Optimizations**: Native access enabled, optimized memory settings
- **Build Performance**: Configuration cache, parallel builds, build caching

### Application Properties
- **Database**: H2 in-memory for development
- **Security**: Basic authentication with admin credentials (admin/admin123)
- **CORS**: Configured for frontend communication
- **Logging**: Comprehensive logging configuration with DEBUG level
- **Actuator**: Health checks, metrics, and Prometheus support

### Environment-Specific Settings
- **Development**: H2 database with console access on port 8081
- **Testing**: In-memory database for fast tests
- **Monitoring**: Actuator endpoints for health and metrics

## 🔧 Troubleshooting

### Java 25 & Gradle 9.1.0 Issues

#### Common Issues and Solutions

1. **Native Access Warnings**
   ```bash
   # Solution: Already configured in gradle.properties
   org.gradle.jvmargs=-Xmx2048m -XX:MaxMetaspaceSize=512m --enable-native-access=ALL-UNNAMED
   ```

2. **Build Performance Issues**
   ```bash
   # Enable all optimizations
   ./gradlew clean build --configuration-cache --parallel --build-cache
   ```

3. **Class File Version Issues**
   - The project uses Java 21 source/target compatibility for maximum compatibility
   - Runtime uses Java 25 for performance benefits

4. **Memory Issues**
   ```bash
   # Increase memory if needed
   export GRADLE_OPTS="-Xmx4g -XX:MaxMetaspaceSize=1g"
   ```

5. **Port Already in Use**
   ```bash
   # Change port in application.properties
   server.port=8082
   ```

### Verification Commands
```bash
# Check Java version
java --version

# Check Gradle version
./gradlew --version

# Verify build
./gradlew clean build

# Test application
./gradlew bootRun
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Angular.js community for the robust frontend framework
- Bootstrap team for the responsive UI framework
- Chart.js team for the visualization library

---

**Built with ❤️ using Java 25, Spring Boot 3.3.5, Gradle 9.1.0, and Angular.js 1.8**