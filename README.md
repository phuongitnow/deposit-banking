# Banking Deposit API

A modern RESTful API for managing banking deposit transactions built with Spring Boot 3, Java 17, and Clean Architecture principles.

## Features

- ✅ **CRUD Operations** for deposit transactions
- ✅ **Spring Boot 3** with Java 17
- ✅ **Clean Architecture** with layered separation
- ✅ **JPA/Hibernate** with PostgreSQL
- ✅ **Liquibase** for database migrations
- ✅ **Bean Validation** for request validation
- ✅ **Global Exception Handling**
- ✅ **Spring Boot Actuator** for monitoring
- ✅ **Unit Tests** with JUnit 5
- ✅ **Docker & Docker Compose** support
- ✅ **Gradle 8.12** build system

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Gradle 8.12**
- **PostgreSQL 16**
- **JPA/Hibernate**
- **Liquibase**
- **Lombok**
- **MapStruct**
- **JUnit 5**
- **Mockito**
- **Docker & Docker Compose**

## Project Structure

The project follows **Clean Architecture** principles with clear layer separation:

```
src/main/java/com/banking/deposit/
├── domain/                 # Domain layer (entities, domain models)
│   └── model/
│       ├── Deposit.java
│       └── DepositStatus.java
├── application/           # Application layer (business logic, DTOs)
│   ├── dto/
│   ├── service/
│   └── exception/
├── infrastructure/        # Infrastructure layer (persistence, external services)
│   └── repository/
├── presentation/         # Presentation layer (controllers, exception handlers)
│   ├── controller/
│   └── exception/
```

## API Endpoints

### Base URL
```
http://localhost:8080/api/v1/deposits
```

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/deposits` | Create a new deposit |
| GET | `/api/v1/deposits/{id}` | Get deposit by ID |
| GET | `/api/v1/deposits` | Get all deposits (with pagination) |
| GET | `/api/v1/deposits/account/{accountNumber}` | Get deposits by account number |
| PATCH | `/api/v1/deposits/{id}/status` | Update deposit status |
| DELETE | `/api/v1/deposits/{id}` | Delete a deposit |

### Example Request

**Create Deposit:**
```json
POST /api/v1/deposits
Content-Type: application/json

{
  "accountNumber": "ACC123456789",
  "amount": 1000.00,
  "currency": "USD",
  "description": "Initial deposit"
}
```

**Response:**
```json
{
  "id": 1,
  "accountNumber": "ACC123456789",
  "amount": 1000.00,
  "status": "PENDING",
  "currency": "USD",
  "description": "Initial deposit",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

## Prerequisites

- Java 17 or higher
- Gradle 8.12 (or use included wrapper)
- Docker and Docker Compose (for containerized deployment)
- PostgreSQL 16 (if running locally without Docker)

## Getting Started

### Option 1: Using Docker Compose (Recommended)

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd deposit-banking
   ```

2. **Start the application with Docker Compose**
   ```bash
   docker compose up -d
   ```

   This will:
   - Start PostgreSQL database
   - Build the application
   - Start the Spring Boot application

3. **Check application health**
   ```bash
   curl http://localhost:8080/actuator/health
   ```

4. **Stop the application**
   ```bash
   docker compose down
   ```

### Option 2: Local Development

1. **Start PostgreSQL** (or use Docker)
   ```bash
   docker run -d \
     --name banking-postgres \
     -e POSTGRES_DB=banking_db \
     -e POSTGRES_USER=banking_user \
     -e POSTGRES_PASSWORD=banking_pass \
     -p 5432:5432 \
     postgres:16-alpine
   ```

2. **Build the application**
   ```bash
   ./gradlew build
   ```

3. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

   Or run the JAR directly:
   ```bash
   java -jar build/libs/deposit-banking-1.0.0.jar
   ```

### Option 3: Using Gradle Wrapper

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run tests
./gradlew test

# Clean build
./gradlew clean build
```

## Testing

### Run Unit Tests
```bash
./gradlew test
```

### Run Tests with Coverage
```bash
./gradlew test jacocoTestReport
```

### Test Coverage Reports
After running tests, coverage reports will be available at:
```
build/reports/jacoco/test/html/index.html
```

## Actuator Endpoints

Spring Boot Actuator provides monitoring and management endpoints:

| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Application health status |
| `/actuator/info` | Application information |
| `/actuator/metrics | Application metrics |
| `/actuator/prometheus` | Prometheus metrics |

Example:
```bash
curl http://localhost:8080/actuator/health
```

## Database Migrations

Liquibase is configured to automatically apply migrations on startup. Migrations are located in:
```
src/main/resources/db/changelog/
```

## Configuration

Application configuration is in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/banking_db
    username: banking_user
    password: banking_pass
```

## Validation Rules

- **Account Number**: 8-20 characters, uppercase letters and numbers only
- **Amount**: Must be > 0, up to 2 decimal places
- **Currency**: 3-character ISO currency code (e.g., USD, EUR)
- **Description**: Max 500 characters

## Error Handling

The application includes global exception handling:

- `404 Not Found` - Resource not found
- `400 Bad Request` - Validation errors or invalid input
- `500 Internal Server Error` - Unexpected errors

Example error response:
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Deposit not found with id: 123",
  "path": "/api/v1/deposits/123"
}
```

## Development

### Code Style
The project follows standard Java conventions and uses Lombok to reduce boilerplate code.

### Dependencies
See `build.gradle` for complete dependency list.

## API Documentation

Once running, explore the API:
- Swagger UI: `http://localhost:8080/swagger-ui.html` (if enabled)
- Health Check: `http://localhost:8080/actuator/health`

## Building for Production

```bash
# Build without tests
./gradlew build -x test

# Build executable JAR
./gradlew bootJar

# Build Docker image
docker build -t deposit-banking:latest .
```

## License

This project is a sample application for educational purposes.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

