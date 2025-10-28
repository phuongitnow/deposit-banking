# 📮 Hướng Dẫn Test API Bằng Postman

## 🎯 Mục Tiêu
Test toàn bộ Banking Deposit API endpoints bằng Postman

## 📋 Chuẩn Bị

### 1. Cài Đặt Postman
- Download: https://www.postman.com/downloads/
- Hoặc dùng web version: https://web.postman.com/

### 2. Đảm Bảo Application Đang Chạy
```bash
# Kiểm tra containers
docker compose ps

# Hoặc nếu chạy local
./gradlew bootRun
```

**Base URL:** `http://localhost:8080`

---

## 🚀 Test Cases

### Endpoint 1: Tạo Deposit Mới

**Request:**
- **Method**: `POST`
- **URL**: `http://localhost:8080/api/v1/deposits`
- **Headers**: 
  ```
  Content-Type: application/json
  ```
- **Body** (JSON):
  ```json
  {
    "accountNumber": "ACC123456789",
    "amount": 1000.00,
    "currency": "USD",
    "description": "Initial deposit"
  }
  ```

**Expected Response (201 Created):**
```json
{
  "id": 1,
  "accountNumber": "ACC123456789",
  "amount": 1000.00,
  "status": "PENDING",
  "currency": "USD",
  "description": "Initial deposit",
  "createdAt": "2025-10-27T03:47:28.94258",
  "updatedAt": "2025-10-27T03:47:28.942612"
}
```

**Test Cases:**
1. ✅ Valid request → Should return 201
2. ❌ Invalid accountNumber (too short) → Should return 400
3. ❌ Invalid amount (negative) → Should return 400
4. ❌ Invalid currency (wrong format) → Should return 400
5. ❌ Missing required fields → Should return 400

---

### Endpoint 2: Lấy Deposit Theo ID

**Request:**
- **Method**: `GET`
- **URL**: `http://localhost:8080/api/v1/deposits/1`
- **Headers**: None

**Expected Response (200 OK):**
```json
{
  "id": 1,
  "accountNumber": "ACC123456789",
  "amount": 1000.00,
  "status": "PENDING",
  "currency": "USD",
  "description": "Initial deposit",
  "createdAt": "2025-10-27T03:47:28.94258",
  "updatedAt": "2025-10-27T03:47:28.942612"
}
```

**Error Response (404 Not Found):**
```json
{
  "timestamp": "2025-10-27T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Deposit not found with id: 999",
  "path": "/api/v1/deposits/999"
}
```

**Test Cases:**
1. ✅ Valid ID → Should return 200
2. ❌ Invalid ID (not exists) → Should return 404
3. ❌ Non-numeric ID → Should return 400

---

### Endpoint 3: Lấy Tất Cả Deposits (Pagination)

**Request:**
- **Method**: `GET`
- **URL**: `http://localhost:8080/api/v1/deposits`
- **Query Parameters**:
  - `page`: 0 (default)
  - `size`: 20 (default)
  - `sort`: createdAt,DESC (default)

**Examples:**
```
http://localhost:8080/api/v1/deposits
http://localhost:8080/api/v1/deposits?page=0&size=10
http://localhost:8080/api/v1/deposits?page=1&size=20&sort=amount,ASC
```

**Expected Response (200 OK):**
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
      "createdAt": "2025-10-27T03:47:28.94258",
      "updatedAt": "2025-10-27T03:47:28.942612"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "unsorted": false
    }
  },
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  "first": true,
  "size": 20,
  "number": 0,
  "numberOfElements": 1,
  "empty": false
}
```

---

### Endpoint 4: Lấy Deposits Theo Account Number

**Request:**
- **Method**: `GET`
- **URL**: `http://localhost:8080/api/v1/deposits/account/ACC123456789`
- **Query Parameters**: 
  - `page`: 0
  - `size`: 20

**Examples:**
```
http://localhost:8080/api/v1/deposits/account/ACC123456789
http://localhost:8080/api/v1/deposits/account/ACC123456789?page=0&size=10
```

**Expected Response:** Same as Endpoint 3

---

### Endpoint 5: Cập Nhật Deposit Status

**Request:**
- **Method**: `PATCH`
- **URL**: `http://localhost:8080/api/v1/deposits/1/status?status=COMPLETED`
- **Query Parameter**: `status` (PENDING, COMPLETED, FAILED, CANCELLED)

**Examples:**
```
http://localhost:8080/api/v1/deposits/1/status?status=COMPLETED
http://localhost:8080/api/v1/deposits/1/status?status=FAILED
http://localhost:8080/api/v1/deposits/1/status?status=CANCELLED
```

**Expected Response (200 OK):**
```json
{
  "id": 1,
  "accountNumber": "ACC123456789",
  "amount": 1000.00,
  "status": "COMPLETED",
  "currency": "USD",
  "description": "Initial deposit",
  "createdAt": "2025-10-27T03:47:28.94258",
  "updatedAt": "2025-10-27T04:00:00.00000"
}
```

**Test Cases:**
1. ✅ Update to COMPLETED
2. ✅ Update to FAILED
3. ✅ Update to CANCELLED
4. ❌ Invalid status value → Should return 400
5. ❌ Invalid ID → Should return 404

---

### Endpoint 6: Xóa Deposit

**Request:**
- **Method**: `DELETE`
- **URL**: `http://localhost:8080/api/v1/deposits/1`

**Expected Response:**
- **Status**: 204 No Content
- **Body**: None

**Test Cases:**
1. ✅ Valid ID → Should return 204
2. ❌ Invalid ID → Should return 404

---

## 📝 Postman Collection Setup

### Cách 1: Tạo Collection Trong Postman

1. **Mở Postman** → New Collection
2. **Đặt tên**: "Banking Deposit API"
3. **Add requests:**

### Cách 2: Import Collection JSON

**Copy JSON sau và import vào Postman:**

```json
{
  "info": {
    "name": "Banking Deposit API",
    "description": "Test collection for Banking Deposit API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Create Deposit",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"accountNumber\": \"ACC123456789\",\n  \"amount\": 1000.00,\n  \"currency\": \"USD\",\n  \"description\": \"Initial deposit\"\n}"
        },
        "url": {
          "raw": "http://localhost:8080/api/v1/deposits",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "v1", "deposits"]
        }
      }
    },
    {
      "name": "Get Deposit by ID",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/api/v1/deposits/1",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "v1", "deposits", "1"]
        }
      }
    },
    {
      "name": "Get All Deposits",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/api/v1/deposits",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "v1", "deposits"]
        }
      }
    },
    {
      "name": "Update Deposit Status",
      "request": {
        "method": "PATCH",
        "url": {
          "raw": "http://localhost:8080/api/v1/deposits/1/status?status=COMPLETED",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "v1", "deposits", "1", "status"],
          "query": [
            {
              "key": "status",
              "value": "COMPLETED"
            }
          ]
        }
      }
    },
    {
      "name": "Delete Deposit",
      "request": {
        "method": "DELETE",
        "url": {
          "raw": "http://localhost:8080/api/v1/deposits/1",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "v1", "deposits", "1"]
        }
      }
    }
  ]
}
```

**Import steps:**
1. Click **Import** trong Postman
2. Paste JSON trên
3. Click **Import**

---

## 🧪 Test Scenarios

### Scenario 1: Create and Get

```
1. POST /api/v1/deposits → Get ID (e.g., 1)
2. GET /api/v1/deposits/1 → Verify data matches
```

### Scenario 2: Create, Update, Verify

```
1. POST /api/v1/deposits → Create with PENDING
2. GET /api/v1/deposits/1 → Verify status = PENDING
3. PATCH /api/v1/deposits/1/status?status=COMPLETED
4. GET /api/v1/deposits/1 → Verify status = COMPLETED, updatedAt changed
```

### Scenario 3: Full CRUD Cycle

```
1. POST /api/v1/deposits → Create
2. GET /api/v1/deposits/1 → Read
3. PATCH /api/v1/deposits/1/status?status=COMPLETED → Update
4. GET /api/v1/deposits/1 → Verify update
5. DELETE /api/v1/deposits/1 → Delete
6. GET /api/v1/deposits/1 → Should return 404
```

### Scenario 4: Validation Tests

```
1. POST with invalid accountNumber (< 8 chars)
   → Should return 400 with validation errors
   
2. POST with negative amount
   → Should return 400
   
3. POST with invalid currency (not 3 chars)
   → Should return 400
   
4. POST with description > 500 chars
   → Should return 400
```

---

## 🔍 Validation Error Response

**Example 400 Response:**
```json
{
  "timestamp": "2025-10-27T10:30:00",
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

---

## ✅ Postman Environment Variables

**Tạo Environment để dễ quản lý:**

1. Click **Environments** → New Environment
2. Add variables:
   - `base_url`: `http://localhost:8080`
   - `api_v1`: `/api/v1`
   - `deposit_id`: `1` (will be updated after create)

3. Update URLs:
   - `{{base_url}}{{api_v1}}/deposits`
   - `{{base_url}}{{api_v1}}/deposits/{{deposit_id}}`

---

## 🎯 Quick Test Checklist

- ✅ Create deposit → Returns 201 with data
- ✅ Get all deposits → Returns paginated list
- ✅ Get by ID → Returns single deposit
- ✅ Get by account → Returns filtered list
- ✅ Update status → Status changes
- ✅ Delete deposit → Returns 204
- ✅ Invalid ID → Returns 404
- ✅ Invalid data → Returns 400 with details

---

## 💡 Tips

1. **Save Responses**: Click "Save Response" để tái sử dụng
2. **Tests Tab**: Viết test scripts để tự động verify
3. **Pre-request Scripts**: Set variables dynamically
4. **Collection Runner**: Chạy tất cả tests cùng lúc
5. **Export/Share**: Export collection để chia sẻ team

---

## 📖 Xem Thêm

- Chi tiết API: `API.md`
- Logic nghiệp vụ: `TOM_TAT_LOGIC_NGHIEP_VU.md`
- Quick start: `QUICKSTART.md`

**Happy Testing! 🚀**

