# 📋 Tóm Tắt Logic Nghiệp Vụ & Cấu Trúc API

## 🎯 Tổng Quan Dự Án

**Banking Deposit API** - Hệ thống quản lý giao dịch tiền gửi ngân hàng
- **Framework**: Spring Boot 3.2.0
- **Database**: PostgreSQL 16
- **Architecture**: Clean Architecture (4 layers)
- **Build**: Gradle 8.12

---

## 🏗️ Kiến Trúc Clean Architecture

### Layer 1: Domain (Domain Layer)
**Vị trí**: `src/main/java/com/banking/deposit/domain/model/`

#### 1.1. Deposit Entity
```java
@Entity
public class Deposit {
    - id: Long (auto-generated)
    - accountNumber: String (số tài khoản)
    - amount: BigDecimal (số tiền)
    - status: DepositStatus (trạng thái)
    - currency: String (đơn vị tiền tệ)
    - description: String (mô tả)
    - createdAt: LocalDateTime (thời gian tạo)
    - updatedAt: LocalDateTime (thời gian cập nhật)
}
```

**Tính năng tự động:**
- `@PrePersist`: Tự động set createdAt, updatedAt khi tạo mới
- `@PreUpdate`: Tự động cập nhật updatedAt khi chỉnh sửa
- Sequence generator cho ID

#### 1.2. DepositStatus Enum
```java
public enum DepositStatus {
    PENDING,    // Đang chờ xử lý
    COMPLETED,  // Đã hoàn thành
    FAILED,     // Thất bại
    CANCELLED   // Đã hủy
}
```

---

### Layer 2: Application (Business Logic)
**Vị trí**: `src/main/java/com/banking/deposit/application/`

#### 2.1. DTOs (Data Transfer Objects)

**DepositRequest** - Dữ liệu nhận vào
```java
- accountNumber: String (validation: 8-20 ký tự, chữ hoa + số)
- amount: BigDecimal (validation: > 0, tối đa 2 chữ số thập phân)
- currency: String (validation: 3 ký tự ISO code)
- description: String (tối đa 500 ký tự)
```

**DepositResponse** - Dữ liệu trả về
```java
- id: Long
- accountNumber: String
- amount: BigDecimal
- status: DepositStatus
- currency: String
- description: String
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

**ApiErrorResponse** - Lỗi trả về
```java
- timestamp: LocalDateTime
- status: int (HTTP status code)
- error: String
- message: String
- path: String
- validationErrors: List<ValidationError>
```

#### 2.2. DepositService (Business Logic)

**Các nghiệp vụ:**

1. **createDeposit()** - Tạo giao dịch mới
   - Nhận DepositRequest
   - Validate dữ liệu (Bean Validation)
   - Map DTO → Entity (DepositMapper)
   - Set status = PENDING mặc định
   - Lưu vào database
   - Trả về DepositResponse

2. **getDepositById()** - Lấy theo ID
   - Tìm trong database
   - Nếu không có → throw ResourceNotFoundException
   - Trả về DepositResponse

3. **getAllDeposits()** - Lấy tất cả (có phân trang)
   - Hỗ trợ Pageable (page, size, sort)
   - Trả về Page<DepositResponse>

4. **getDepositsByAccountNumber()** - Lấy theo số tài khoản
   - Tìm theo accountNumber
   - Có phân trang
   - Trả về Page<DepositResponse>

5. **updateDepositStatus()** - Cập nhật trạng thái
   - Tìm deposit theo ID
   - Kiểm tra tồn tại
   - Update status
   - Lưu vào database
   - Trả về DepositResponse mới

6. **deleteDeposit()** - Xóa giao dịch
   - Kiểm tra tồn tại
   - Xóa khỏi database

**Đặc điểm:**
- `@Transactional` cho tất cả methods
- `@Transactional(readOnly = true)` cho get methods
- Logging mọi thao tác
- Exception handling

#### 2.3. DepositMapper
- Chuyển đổi DepositRequest → Deposit (Entity)
- Chuyển đổi Deposit (Entity) → DepositResponse
- Sử dụng MapStruct (compile-time)

#### 2.4. Exceptions
- **ResourceNotFoundException**: Khi không tìm thấy resource
- **ValidationException**: Khi dữ liệu không hợp lệ

---

### Layer 3: Infrastructure (Persistence)
**Vị trí**: `src/main/java/com/banking/deposit/infrastructure/repository/`

#### DepositRepository
```java
public interface DepositRepository extends JpaRepository<Deposit, Long> {
    
    // Tìm theo accountNumber có phân trang
    Page<Deposit> findByAccountNumber(String accountNumber, Pageable pageable);
    
    // Tìm theo status
    List<Deposit> findByStatus(DepositStatus status);
    
    // Tìm theo ID và accountNumber
    Optional<Deposit> findByIdAndAccountNumber(Long id, String accountNumber);
    
    // Kiểm tra tồn tại
    boolean existsByAccountNumber(String accountNumber);
    
    // Đếm số lượng
    long countByAccountNumber(String accountNumber);
}
```

**Features:**
- Custom queries với Spring Data JPA
- Tự động implement bởi Spring
- Type-safe

---

### Layer 4: Presentation (API Layer)
**Vị trí**: `src/main/java/com/banking/deposit/presentation/`

#### DepositController
**Base URL**: `/api/v1/deposits`

**Endpoints:**

1. **POST** `/api/v1/deposits`
   - Tạo deposit mới
   - Input: DepositRequest (JSON)
   - Output: DepositResponse
   - Status: 201 Created

2. **GET** `/api/v1/deposits/{id}`
   - Lấy theo ID
   - Output: DepositResponse
   - Status: 200 OK
   - Error: 404 Not Found

3. **GET** `/api/v1/deposits`
   - Lấy tất cả (paginated)
   - Query params: page, size, sort
   - Output: Page<DepositResponse>
   - Status: 200 OK

4. **GET** `/api/v1/deposits/account/{accountNumber}`
   - Lấy theo account number
   - Query params: page, size
   - Output: Page<DepositResponse>
   - Status: 200 OK

5. **PATCH** `/api/v1/deposits/{id}/status?status={status}`
   - Cập nhật status
   - Query param: status (enum)
   - Output: DepositResponse
   - Status: 200 OK

6. **DELETE** `/api/v1/deposits/{id}`
   - Xóa deposit
   - Status: 204 No Content
   - Error: 404 Not Found

**Đặc điểm:**
- `@Valid` cho validation
- Pagination mặc định: 20 items/page
- Sort mặc định: createdAt DESC
- RESTful conventions

#### GlobalExceptionHandler
**Xử lý lỗi toàn cục:**

1. **ResourceNotFoundException** → 404 Not Found
2. **ValidationException** → 400 Bad Request
3. **MethodArgumentNotValidException** → 400 Bad Request (với chi tiết validation)
4. **IllegalArgumentException** → 400 Bad Request
5. **Exception** (general) → 500 Internal Server Error

**Response format:**
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

## 💾 Database Schema

### Deposits Table
```sql
CREATE TABLE deposits (
    id BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(50) NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Indexes
CREATE INDEX idx_deposits_account_number ON deposits(account_number);
CREATE INDEX idx_deposits_status ON deposits(status);
CREATE INDEX idx_deposits_created_at ON deposits(created_at);
```

**Migration:**
- Sử dụng Liquibase
- File: `src/main/resources/db/changelog/changelog/001-create-deposits-table.yaml`
- Tự động chạy khi application start

---

## 🔄 Flow Logic Nghiệp Vụ

### Flow 1: Tạo Deposit Mới
```
Client Request (POST /api/v1/deposits)
    ↓
Controller @Valid DepositRequest
    ↓
Validation (Bean Validation)
    ├── accountNumber: 8-20 chars, uppercase+numbers
    ├── amount: > 0, max 2 decimals
    ├── currency: 3 chars ISO code
    └── description: max 500 chars
    ↓
Service.createDeposit()
    ↓
Mapper: DepositRequest → Deposit Entity
    ↓
Set status = PENDING
Set createdAt, updatedAt = now()
    ↓
Repository.save()
    ↓
Response: DepositResponse
    ↓
HTTP 201 Created
```

### Flow 2: Cập Nhật Status
```
Client Request (PATCH /api/v1/deposits/1/status?status=COMPLETED)
    ↓
Controller
    ↓
Service.updateDepositStatus(id, status)
    ↓
Repository.findById(id)
    ├── Found → Get entity
    └── Not Found → throw ResourceNotFoundException
    ↓
Entity.setStatus(status)
Entity.setUpdatedAt(now())
    ↓
Repository.save()
    ↓
Response: DepositResponse
    ↓
HTTP 200 OK
```

### Flow 3: Xử Lý Lỗi
```
Exception occurred
    ↓
GlobalExceptionHandler catches
    ↓
Determine exception type
    ├── ResourceNotFoundException → 404
    ├── ValidationException → 400
    ├── MethodArgumentNotValidException → 400 (with details)
    └── Others → 500
    ↓
Build ApiErrorResponse
    ↓
Return JSON with error details
```

---

## ✅ Validation Rules

### Account Number
- Required: Yes
- Length: 8-20 characters
- Pattern: Only uppercase letters (A-Z) and numbers (0-9)
- Regex: `^[A-Z0-9]+$`

### Amount
- Required: Yes
- Minimum: 0.01
- Precision: 19 digits, 2 decimal places
- Format: `12345678901234567.89`

### Currency
- Required: Yes
- Format: ISO 4217 code
- Length: Exactly 3 characters
- Examples: USD, EUR, GBP, VND
- Pattern: `^[A-Z]{3}$`

### Description
- Required: No
- Max Length: 500 characters

---

## 🧪 Testing Strategy

### Unit Tests
- `DepositServiceTest`: Test business logic
  - Create deposit
  - Get by ID
  - Update status
  - Delete
  - Error cases
  
- `DepositControllerTest`: Test API layer
  - HTTP status codes
  - Request/response format
  - Validation

### Integration Tests
- Database connection
- Liquibase migrations
- End-to-end API calls

---

## 📊 API Summary Table

| Method | Endpoint | Function | Input | Output | Status |
|--------|----------|----------|-------|--------|--------|
| POST | `/api/v1/deposits` | Create | DepositRequest | DepositResponse | 201 |
| GET | `/api/v1/deposits/{id}` | Get by ID | - | DepositResponse | 200 |
| GET | `/api/v1/deposits` | Get all | Pageable | Page<DepositResponse> | 200 |
| GET | `/api/v1/deposits/account/{account}` | Get by account | - | Page<DepositResponse> | 200 |
| PATCH | `/api/v1/deposits/{id}/status` | Update status | status param | DepositResponse | 200 |
| DELETE | `/api/v1/deposits/{id}` | Delete | - | - | 204 |

---

## 🔐 Security & Best Practices

1. **Validation**: Dùng Bean Validation ở cả client và server
2. **Transactions**: Tất cả database operations đều transactional
3. **Logging**: Log mọi thao tác để audit
4. **Error Handling**: Global exception handler cho consistent errors
5. **ID Generation**: Sequence generator cho database performance
6. **Timestamps**: Tự động quản lý created/updated timestamps
7. **Clean Code**: Separation of concerns rõ ràng

---

## 🎯 Key Points

✅ **Clean Architecture**: Rõ ràng 4 layers
✅ **RESTful**: Tuân thủ REST conventions  
✅ **Validation**: Multi-level validation
✅ **Error Handling**: Comprehensive error management
✅ **Pagination**: Efficient data retrieval
✅ **Logging**: Full audit trail
✅ **Testing**: Unit + Integration tests
✅ **Migration**: Database versioning với Liquibase

**Đây là một production-ready banking API!**

