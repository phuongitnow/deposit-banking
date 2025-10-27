# Build Instructions

## Quick Start

### Option 1: Docker Compose (Recommended)

This is the easiest way to run the application:

```bash
# Build and start all services
docker compose up -d

# Check if services are running
docker compose ps

# View application logs
docker compose logs -f app

# Stop all services
docker compose down

# Stop and remove volumes (cleans database)
docker compose down -v
```

### Option 2: Local Build with Gradle

```bash
# Build the project
./gradlew clean build

# Run tests
./gradlew test

# Run the application
./gradlew bootRun

# Or run the JAR directly
java -jar build/libs/deposit-banking-1.0.0.jar
```

**Note**: Make sure PostgreSQL is running on port 5432 with the credentials configured in `application.yml`.

---

## Build Details

### Project Statistics

- **Java Files**: 17 source files
- **Test Files**: 2 test classes
- **Dependencies**: Spring Boot 3.2.0, PostgreSQL, Liquibase, MapStruct

### Build Commands

```bash
# Clean build
./gradlew clean build

# Build without tests
./gradlew build -x test

# Run tests only
./gradlew test

# Build JAR
./gradlew bootJar

# Generate dependency report
./gradlew dependencies
```

---

## Docker Build

### Build Docker Image

```bash
# Build the image
docker compose build

# Build without cache
docker compose build --no-cache

# Build specific service
docker compose build app
```

### Running Containers

```bash
# Start all services
docker compose up -d

# Start in foreground (see logs)
docker compose up

# Restart a service
docker compose restart app

# Stop a service
docker compose stop app

# View logs
docker compose logs -f app postgres
```

---

## Database Setup

### With Docker

The database is automatically set up by docker-compose.

### Manual PostgreSQL Setup

```bash
# Using Docker
docker run -d \
  --name banking-postgres \
  -e POSTGRES_DB=banking_db \
  -e POSTGRES_USER=banking_user \
  -e POSTGRES_PASSWORD=banking_pass \
  -p 5432:5432 \
  postgres:16-alpine

# Using local installation
createdb banking_db
```

Migrations will run automatically on startup via Liquibase.

---

## Testing

### Run All Tests

```bash
./gradlew test
```

### Run Specific Test

```bash
./gradlew test --tests DepositServiceTest
```

### Test with Coverage

```bash
./gradlew test jacocoTestReport
```

Coverage report location: `build/reports/jacoco/test/html/index.html`

---

## Development Mode

### IDE Setup (IntelliJ IDEA)

1. Open project
2. Import Gradle project
3. Wait for dependency download
4. Run `DepositBankingApplication`
5. Open browser: http://localhost:8080

### Hot Reload (Optional)

Add Spring Boot DevTools to `build.gradle`:

```gradle
dependencies {
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
}
```

---

## Troubleshooting

### Port Already in Use

Change port in `src/main/resources/application.yml`:

```yaml
server:
  port: 8081
```

### Database Connection Issues

Check PostgreSQL is running:
```bash
docker ps | grep postgres
```

Check connection in `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/banking_db
```

### Build Fails

Clean and rebuild:
```bash
./gradlew clean build
```

Or delete build directories:
```bash
rm -rf build .gradle
./gradlew build
```

---

## Deployment

### Production JAR

```bash
./gradlew bootJar
```

JAR location: `build/libs/deposit-banking-1.0.0.jar`

### Production Docker

```bash
docker compose -f docker-compose.prod.yml up -d
```

---

## Useful Commands

```bash
# Check Java version
java -version

# Check Docker
docker --version
docker compose version

# Check application health
curl http://localhost:8080/actuator/health

# Create a test deposit
curl -X POST http://localhost:8080/api/v1/deposits \
  -H "Content-Type: application/json" \
  -d '{"accountNumber":"TEST123","amount":100.00,"currency":"USD"}'
```

