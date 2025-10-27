# Getting Started with Banking Deposit API

## 🚀 Quick Start (5 Minutes)

### Prerequisites
- Docker and Docker Compose installed
- OR Java 17 and PostgreSQL installed

### Start the Application

**With Docker (Easiest):**
```bash
docker compose up -d
```

**Without Docker:**
```bash
# Start PostgreSQL first
docker run -d -p 5432:5432 -e POSTGRES_DB=banking_db \
  -e POSTGRES_USER=banking_user \
  -e POSTGRES_PASSWORD=banking_pass \
  postgres:16-alpine

# Start the application
./gradlew bootRun
```

### Verify It's Running

```bash
# Check health
curl http://localhost:8080/actuator/health

# Should return: {"status":"UP"}
```

### Test the API

```bash
# Create a deposit
curl -X POST http://localhost:8080/api/v1/deposits \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "ACC123456789",
    "amount": 1000.00,
    "currency": "USD",
    "description": "My first deposit"
  }'

# Get all deposits
curl http://localhost:8080/api/v1/deposits

# Get by ID
curl http://localhost:8080/api/v1/deposits/1
```

## 📚 More Information

- **Full Documentation**: See [README.md](README.md)
- **API Reference**: See [API.md](API.md)
- **Architecture**: See [ARCHITECTURE.md](ARCHITECTURE.md)
- **Quick Start**: See [QUICKSTART.md](QUICKSTART.md)

## 🛠️ What's Inside?

- ✅ 17 Java source files
- ✅ 2 test classes
- ✅ Clean Architecture pattern
- ✅ Complete CRUD operations
- ✅ Database migrations
- ✅ Docker support
- ✅ Comprehensive documentation

## 🎯 Next Steps

1. Explore the API documentation
2. Read the architecture guide
3. Run the tests
4. Start building your features!

Happy Coding! 🎉
