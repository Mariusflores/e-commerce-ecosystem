# E-Commerce Ecosystem

A **microservices-based e-commerce platform** built with Spring Boot, demonstrating event-driven architecture, service
discovery, and distributed systems concepts.

**Status:** Paused Development as of Dec 2025 in favor of a Job Application Tracker App
with plans for deployment. Aiming to come back stronger

---

## Architecture Overview

This project implements a **microservices architecture** with the following services:

### Core Services

- **API Gateway** - Entry point for all client requests, routes to appropriate services
- **User Service** - User authentication, registration, and JWT token management
- **Product Service** - Product catalog management and CRUD operations
- **Inventory Service** - Stock management, product availability tracking
- **Order Service** - Order creation, status tracking, and order history
- **Payments Service** - Payment processing simulation with realistic failure scenarios

### Infrastructure

- **RabbitMQ** - Event-driven messaging between services
- **MySQL** - Persistent data storage for each service
- **Consul** - Service discovery and health monitoring
- **Docker Compose** - Container orchestration for all services

---

## Technology Stack

- **Backend:** Spring Boot 3.5.5, Java 21
- **Security:** Spring Security 6.5.3, JWT Authentication
- **Messaging:** RabbitMQ with Topic Exchanges
- **Database:** MySQL 8
- **Service Discovery:** Consul
- **Build Tool:** Maven (multi-module project)
- **Containerization:** Docker & Docker Compose

---

## Key Features

### Event-Driven Architecture

- **Order Placement Flow:**
    1. Order created → `OrderPlacedEvent` published
    2. Payment Service processes payment (80% success, 20% failure simulation)
    3. Inventory Service reserves stock
    4. Payment result → `PaymentCompletedEvent` or `PaymentFailedEvent`
    5. Order status updated to CONFIRMED or CANCELLED

### Service Communication

- **Synchronous:** Feign clients for REST API calls (e.g., order-service → inventory-service)
- **Asynchronous:** RabbitMQ for event-driven workflows
- **Service Discovery:** Consul for dynamic service location

### Security

- JWT-based authentication with stateless session management
- Password encryption with BCrypt
- Secured endpoints with role-based access control

### Payment Simulation

- Realistic payment processing with 1-4 second delays
- 80/20 success/failure ratio
- Multiple failure scenarios: INSUFFICIENT_FUNDS, CARD_DECLINED, NETWORK_ERROR, GATEWAY_TIMEOUT

---

## Project Structure

```
e-commerce-ecosystem/
├── api-gateway/          # API Gateway service
├── user-service/         # User authentication & management
├── product-service/      # Product catalog
├── inventory-service/    # Stock management
├── order-service/        # Order processing
├── payments-service/     # Payment simulation
├── domain/               # Shared DTOs and domain models
├── docker-compose.yml    # Multi-container orchestration
└── scripts/              # Database initialization scripts
```

---

## Event Flow

### Message Queues & Exchanges

**Order Events:**

- Exchange: `order-exchange`
- Queues: `payment-order-queue`, `inventory-order-queue`
- Routing Key: `order.placed`

**Payment Events:**

- Exchange: `payment-exchange`
- Queues: `payment-completed-queue`, `payment-failed-queue`
- Routing Keys: `payment.completed`, `payment.failed`

**Product Events:**

- Exchange: `product-exchange`
- Queue: `product-queue`
- Routing Keys: `product.created`, `product.deleted`

---

## Running the Project

### Prerequisites

- Docker Desktop
- Maven 3.8+
- Java 21

### Environment Configuration

Create a `.env` file in the project root with the following variables:

```env
# RabbitMQ Configuration
RABBIT_USERNAME=your_rabbitmq_username
RABBIT_PASSWORD=your_rabbitmq_password

# MongoDB Configuration
MONGODB_USER=your_mongodb_user
MONGODB_PASSWORD=your_mongodb_password
MONGODB_HOST=mongodb

# MySQL Configuration
MYSQL_INVENTORY_DB=inventory_db
MYSQL_ORDER_DB=order_db
MYSQL_USER_DB=user_db
MYSQL_PAYMENTS_DB=payments_db
MYSQL_USER=your_mysql_user
MYSQL_PASS=your_mysql_password
MYSQL_ROOT_PASS=your_root_password

# JWT Configuration
JWT_SECRET=f7a4e8c1b6d9f3a7e2c5b8d4f1a6e9c3b7d2f5a3f8a9c2e7b1d4f6a8e3c5b9d2
JWT_EXPIRATION=86400000
```

### Start All Services

```bash
docker-compose up -d --build
```

### Access Points

- **API Gateway:** http://localhost:8080
- **RabbitMQ Management:** http://localhost:15672 (guest/guest)
- **Consul UI:** http://localhost:8500

---

## API Endpoints

### User Service (via API Gateway)

- `POST /api/users/register` - Register new user
- `POST /api/users/login` - Authenticate and receive JWT token

### Product Service

- `POST /api/products` - Create product
- `GET /api/products` - List all products
- `GET /api/products/{id}` - Get product by ID

### Inventory Service

- `GET /api/inventory` - Check stock for SKU codes
- `POST /api/inventory` - Add inventory items

### Order Service

- `POST /api/orders` - Place new order
- `GET /api/orders/{orderNumber}` - Get order details
- `GET /api/orders/customer/{customerId}` - Get customer's orders

---

## Learning Outcomes

This project demonstrates:

✅ **Microservices Architecture** - Service decomposition, bounded contexts, and independent deployability  
✅ **Event-Driven Design** - Asynchronous communication with RabbitMQ topic exchanges  
✅ **Service Discovery** - Dynamic service registration and discovery with Consul  
✅ **API Gateway Pattern** - Centralized routing and cross-cutting concerns  
✅ **JWT Authentication** - Stateless security with token-based auth  
✅ **Docker Orchestration** - Multi-container deployment with health checks  
✅ **Transaction Management** - Handling distributed transactions and eventual consistency  
✅ **Error Handling** - Resilience patterns and failure scenarios

---

## Future Enhancements

- [ ] Frontend (React + Tailwind CSS)
- [ ] API Gateway authentication/authorization
- [ ] Guest checkout functionality
- [ ] Payment retry logic and dead-letter queues
- [ ] Distributed tracing (Zipkin/Jaeger)
- [ ] Circuit breakers (Resilience4j)
- [ ] Kubernetes deployment
- [ ] Integration and E2E testing

---

## Notes

This is a **learning project** focused on understanding microservices architecture, event-driven systems, and Spring
Boot ecosystem. The payment system is simulated for educational purposes.
