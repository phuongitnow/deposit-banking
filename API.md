# API Documentation

## Base URL
```
http://localhost:8080/api/v1
```

## Endpoints Overview

All endpoints follow RESTful conventions and return JSON responses.

### 1. Create Deposit
**POST** `/api/v1/deposits`

Creates a new deposit transaction.

**Request Body:**
```json
{
  "accountNumber": "ACC123456789",
  "amount": 1000.00,
  "currency": "USD",
  "description": "Initial deposit"
}
```

**Response:** `201 Created`
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

---

### 2. Get Deposit by ID
**GET** `/api/v1/deposits/{id}`

Retrieves a specific deposit by its ID.

**Response:** `200 OK`
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

**Error Response:** `404 Not Found`
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Deposit not found with id: 123",
  "path": "/api/v1/deposits/123"
}
```

---

### 3. Get All Deposits
**GET** `/api/v1/deposits`

Retrieves all deposits with pagination support.

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)
- `sort` (optional): Sort field (default: createdAt,DESC)

**Examples:**
```bash
GET /api/v1/deposits?page=0&size=10
GET /api/v1/deposits?page=1&size=20&sort=amount,DESC
```

**Response:** `200 OK`
```json
{
  "content": [
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
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 1,
  "totalPages": 1
}
```

---

### 4. Get Deposits by Account Number
**GET** `/api/v1/deposits/account/{accountNumber}`

Retrieves all deposits for a specific account.

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)
- `sort` (optional): Sort field (default: createdAt,DESC)

**Example:**
```bash
GET /api/v1/deposits/account/ACC123456789?page=0&size=10
```

**Response:** `200 OK`
Similar to "Get All Deposits" response.

---

### 5. Update Deposit Status
**PATCH** `/api/v1/deposits/{id}/status`

Updates the status of a deposit.

**Query Parameters:**
- `status`: Status value (PENDING, COMPLETED, FAILED, CANCELLED)

**Example:**
```bash
PATCH /api/v1/deposits/1/status?status=COMPLETED
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "accountNumber": "ACC123456789",
  "amount": 1000.00,
  "status": "COMPLETED",
  "currency": "USD",
  "description": "Initial deposit",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T11:00:00"
}
```

---

### 6. Delete Deposit
**DELETE** `/api/v1/deposits/{id}`

Deletes a deposit by its ID.

**Response:** `204 No Content`

**Error Response:** `404 Not Found`

---

## Deposit Status Values

- **PENDING**: Deposit is pending processing
- **COMPLETED**: Deposit has been successfully processed
- **FAILED**: Deposit processing failed
- **CANCELLED**: Deposit has been cancelled

---

## Validation Rules

### Account Number
- **Required**: Yes
- **Format**: Uppercase letters and numbers only
- **Length**: 8-20 characters
- **Pattern**: `^[A-Z0-9]+$`

### Amount
- **Required**: Yes
- **Minimum**: 0.01
- **Precision**: Up to 2 decimal places
- **Digits**: Max 15 integer digits, 2 decimal digits

### Currency
- **Required**: Yes
- **Format**: 3-character ISO currency code (e.g., USD, EUR, GBP)
- **Pattern**: Uppercase letters only

### Description
- **Required**: No
- **Max Length**: 500 characters

---

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/deposits",
  "validationErrors": [
    {
      "field": "accountNumber",
      "message": "Account number must be between 8 and 20 characters"
    },
    {
      "field": "amount",
      "message": "Amount must be greater than 0"
    }
  ]
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Deposit not found with id: 123",
  "path": "/api/v1/deposits/123"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred",
  "path": "/api/v1/deposits"
}
```

---

## Actuator Endpoints

### Health Check
```bash
GET /actuator/health
```

**Response:**
```json
{
  "status": "UP"
}
```

### Application Info
```bash
GET /actuator/info
```

### Metrics
```bash
GET /actuator/metrics
```

### Prometheus Metrics
```bash
GET /actuator/prometheus
```

---

## Examples with cURL

### Create a deposit
```bash
curl -X POST http://localhost:8080/api/v1/deposits \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "ACC123456789",
    "amount": 1000.00,
    "currency": "USD",
    "description": "Initial deposit"
  }'
```

### Get a deposit
```bash
curl http://localhost:8080/api/v1/deposits/1
```

### Update deposit status
```bash
curl -X PATCH "http://localhost:8080/api/v1/deposits/1/status?status=COMPLETED"
```

### Delete a deposit
```bash
curl -X DELETE http://localhost:8080/api/v1/deposits/1
```

---

## Testing the API

Use any REST client or the examples above:
- **cURL**: Command-line tool
- **Postman**: GUI tool for API testing
- **HTTPie**: Modern command-line HTTP client
- **Rest Assured**: Java testing library

