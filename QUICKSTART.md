# Quick Start Guide

This guide will help you get the Banking Deposit API up and running quickly.

## Prerequisites

Before you begin, ensure you have:
- Java 17 or higher
- Docker and Docker Compose installed
- (Optional) Gradle 8.12

## Option 1: Using Docker (Recommended - Fastest)

### Step 1: Start the Application

```bash
# Clone or navigate to the project directory
cd deposit-banking

# Start all services with Docker Compose
docker compose up -d
```

This command will:
- Download PostgreSQL 16 image
- Build the Spring Boot application
- Start both database and application containers

### Step 2: Verify the Application

```bash
# Check if containers are running
docker compose ps

# Check application health
curl http://localhost:8080/actuator/health

# Check application info
curl http://localhost:8080/actuator/info
```

### Step 3: Test the API

```bash
# Create a new deposit
curl -X POST http://localhost:8080/api/v1/deposits \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "ACC123456789",
    "amount": 1000.00,
    "currency": "USD",
    "description": "Initial deposit"
  }'

# Get all deposits
curl http://localhost:8080/api/v1/deposits

# Get deposit by ID
curl http://localhost:8080/api/v1/deposits/1
```

### Step 4: Stop the Application

```bash
docker compose down
```

To also remove volumes (clears database):
```bash
docker compose down -v
```

---

## Option 2: Local Development

### Step 1: Start PostgreSQL Database

Using Docker (recommended):
```bash
docker run -d \
  --name banking-postgres \
  -e POSTGRES_DB=banking_db \
  -e POSTGRES_USER=banking_user \
  -e POSTGRES_PASSWORD=banking_pass \
  -p 5432:5432 \
  postgres:16-alpine
```

Or using a local PostgreSQL installation.

### Step 2: Build and Run

```bash
# Make gradlew executable (if not already)
chmod +x gradlew

# Build the application
./gradlew build

# Run the application
./gradlew bootRun
```

Or using the JAR:
```bash
java -jar build/libs/deposit-banking-1.0.0.jar
```

### Step 3: Test the Application

Same as Option 1, Step 3.

---

## Option 3: Using IDE (IntelliJ IDEA / Eclipse)

### Step 1: Import Project

1. Open IntelliJ IDEA or Eclipse
2. Select "Import Gradle Project"
3. Choose the project directory
4. Wait for indexing and dependency download

### Step 2: Configure Run Configuration

**IntelliJ IDEA:**
1. Right-click on `DepositBankingApplication.java`
2. Select "Run 'DepositBankingApplication'"

Or create a Spring Boot run configuration:
- Main class: `com.banking.deposit.DepositBankingApplication`
- Profile: (leave empty or use `test` for tests)

### Step 3: Run Tests

```bash
# Using Gradle
./gradlew test

# Or using IDE test runner
# Right-click on test file and select "Run Tests"
```

---

## Running Tests

### Unit Tests

```bash
./gradlew test
```

### Specific Test Class

```bash
./gradlew test --tests DepositServiceTest
```

### With Coverage

```bash
./gradlew test jacocoTestReport
```

### View Coverage Report

Open in browser:
```
build/reports/jacoco/test/html/index.html
```

---

## Common Tasks

### Build Without Tests

```bash
./gradlew build -x test
```

### Clean Build

```bash
./gradlew clean build
```

### Run with Different Profile

```bash
./gradlew bootRun --args='--spring.profiles.active=test'
```

### Check Logs

```bash
# Docker logs
docker compose logs -f app

# Or application logs
tail -f logs/application.log
```

---

## Troubleshooting

### Port Already in Use

If port 8080 is already in use:

**Docker:**
Edit `docker-compose.yml` (or use `docker compose config`):
```yaml
app:
  ports:
    - "8081:8080"  # Change to your preferred port
```

**Local:**
Edit `src/main/resources/application.yml`:
```yaml
server:
  port: 8081
```

### Database Connection Issues

1. Check if PostgreSQL is running:
   ```bash
   docker ps | grep postgres
   ```

2. Verify connection string in `application.yml`

3. Check logs:
   ```bash
   docker compose logs app
   ```

### Test Failures

```bash
# Clean and rebuild
./gradlew clean test

# Run with debug logging
./gradlew test --debug
```

---

## Next Steps

1. **Explore the API**: Use the `/api/v1/deposits` endpoints
2. **Check Documentation**: See `API.md` for complete API reference
3. **Review Architecture**: See `ARCHITECTURE.md` for design details
4. **Read README**: See `README.md` for comprehensive information

## Useful Commands

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run tests
./gradlew test

# Start with Docker
docker compose up -d

# View logs
docker compose logs -f

# Stop application
docker compose down

# Clean everything
./gradlew clean
docker compose down -v
```

## Getting Help

- Check logs: `docker compose logs app`
- Read documentation: `README.md`, `API.md`, `ARCHITECTURE.md`
- View health: http://localhost:8080/actuator/health

---

Happy Coding! 🚀

