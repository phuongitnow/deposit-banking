# Architecture Documentation

## Overview

This project follows **Clean Architecture** principles with clear separation of concerns across four distinct layers. This design promotes maintainability, testability, and scalability.

## Architecture Layers

### 1. Domain Layer (Core)
**Package**: `com.banking.deposit.domain`

The domain layer contains the core business logic and entities. It has no dependencies on external frameworks or libraries.

**Components:**
- **Entities**: `Deposit`, `DepositStatus`
- **Business Rules**: Domain-specific validation and logic

**Key Features:**
- Pure business logic
- No framework dependencies
- Used by all other layers

### 2. Application Layer (Use Cases)
**Package**: `com.banking.deposit.application`

The application layer contains use cases and application-specific business logic. It defines what the application does.

**Components:**
- **DTOs**: `DepositRequest`, `DepositResponse`, `ApiErrorResponse`
- **Services**: `DepositService` (use cases)
- **Mappers**: `DepositMapper` (DTO-Entity conversion)
- **Exceptions**: `ResourceNotFoundException`, `ValidationException`

**Key Features:**
- Use case implementation
- Application-specific business rules
- Depends only on domain layer
- No presentation or infrastructure concerns

### 3. Infrastructure Layer
**Package**: `com.banking.deposit.infrastructure`

The infrastructure layer handles external concerns like database persistence, external services, etc.

**Components:**
- **Repositories**: `DepositRepository` (JPA repositories)
- Database configurations
- External service integrations

**Key Features:**
- Implements interfaces defined in application layer
- Handles technical details
- Framework-specific (JPA, Liquibase)
- Depends on domain and application layers

### 4. Presentation Layer
**Package**: `com.banking.deposit.presentation`

The presentation layer handles user interactions (HTTP requests/responses).

**Components:**
- **Controllers**: `DepositController` (REST endpoints)
- **Exception Handlers**: `GlobalExceptionHandler` (error handling)

**Key Features:**
- HTTP endpoint definitions
- Request/response handling
- Input validation
- Error handling
- Depends on all other layers

## Data Flow

```
HTTP Request
    ↓
Presentation Layer (Controller)
    ↓
Application Layer (Service)
    ↓
Infrastructure Layer (Repository)
    ↓
Database (PostgreSQL)
```

## Dependency Rule

The dependency rule states that dependencies should point inward:
- **Presentation** → **Application** → **Domain**
- **Infrastructure** → **Application** → **Domain**

This ensures that:
1. Domain layer is independent of other layers
2. Business logic is framework-agnostic
3. Easy to test and maintain

## Package Structure

```
com.banking.deposit
├── domain/                    # Business logic
│   └── model/
│       ├── Deposit.java
│       └── DepositStatus.java
│
├── application/               # Use cases
│   ├── dto/                   # Data Transfer Objects
│   ├── service/               # Business logic
│   └── exception/             # Application exceptions
│
├── infrastructure/            # Technical implementation
│   └── repository/            # Data access
│
└── presentation/              # User interface
    ├── controller/            # REST endpoints
    └── exception/             # Global error handling
```

## Design Patterns Used

### 1. Repository Pattern
- Abstracts data access logic
- Makes database operations interchangeable
- Enables easy testing with mock repositories

### 2. DTO Pattern
- Separates external DTOs from internal entities
- Prevents direct exposure of domain models
- Provides control over API contracts

### 3. Service Layer Pattern
- Encapsulates business logic
- Coordinates between repositories and controllers
- Centralizes transaction management

### 4. Dependency Injection
- Loose coupling between components
- Easier testing and maintenance
- Managed by Spring framework

### 5. Mapper Pattern (MapStruct)
- Automates DTO-Entity conversion
- Compile-time code generation
- Type-safe mapping

## Testing Strategy

### Unit Tests
- **Location**: `src/test/java`
- **Coverage**: Service layer, Controller layer
- **Tools**: JUnit 5, Mockito

### Integration Tests
- Database integration with Testcontainers
- End-to-end API tests
- Spring Boot Test context

## Benefits of This Architecture

1. **Maintainability**: Clear separation makes code easy to understand and modify
2. **Testability**: Each layer can be tested independently
3. **Scalability**: Easy to add new features or change implementations
4. **Flexibility**: Can swap implementations without affecting other layers
5. **Independence**: Business logic is independent of frameworks
6. **Team Collaboration**: Different teams can work on different layers

## Technology Integration

- **Spring Boot**: Provides dependency injection and auto-configuration
- **JPA/Hibernate**: ORM for database operations
- **Liquibase**: Database migration management
- **MapStruct**: Compile-time DTO mapping
- **Lombok**: Reduces boilerplate code
- **Bean Validation**: Request validation
- **Actuator**: Application monitoring and metrics

## Future Enhancements

1. Add more domain entities (Account, Transaction, etc.)
2. Implement CQRS pattern for read/write separation
3. Add event-driven architecture
4. Implement API versioning strategy
5. Add GraphQL support
6. Enhance security with OAuth2/JWT
7. Add caching layer (Redis)
8. Implement message queue for async processing

## References

- Clean Architecture by Robert C. Martin
- Domain-Driven Design by Eric Evans
- Spring Boot Documentation
- Java Best Practices

