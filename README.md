# Nail Services Marketplace Application

This is a Spring Boot application that provides the backend API for a nail services marketplace platform.

## Prerequisites

- Java 17 or higher
- PostgreSQL 17
- Maven

## Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE nail_services_db;
```

2. Update the database connection settings in `src/main/resources/application.yml` if needed:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/nail_services_db
    username: your_username
    password: your_password
```

## Building and Running

1. Build the project:
```bash
mvn clean install
```

2. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api`

## API Documentation

Once the application is running, you can access the API documentation at:
- Swagger UI: `http://localhost:8080/api/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api/api-docs`

## Project Structure

```
src/main/java/com/nailservices/
├── config/         # Configuration classes
├── controller/     # REST controllers
├── dto/           # Data Transfer Objects
├── entity/        # JPA entities
├── repository/    # Spring Data JPA repositories
├── service/       # Business logic
└── security/      # Security configuration
```

## Features

- User authentication and authorization
- Customer and provider profile management
- Service management
- Appointment scheduling
- Review system
- Payment processing
- Portfolio management

## Security

The application uses JWT (JSON Web Tokens) for authentication. Make sure to:
1. Update the JWT secret key in `application.yml`
2. Use HTTPS in production
3. Follow security best practices when deploying
