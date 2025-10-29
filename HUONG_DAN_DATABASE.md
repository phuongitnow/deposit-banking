# 🗄️ Hướng Dẫn Database Schema & Truy Cập

## 📊 Tổng Quan Database

### Thông Tin Kết Nối

```yaml
Database Type: PostgreSQL 16
Host: localhost (hoặc banking-postgres nếu trong Docker)
Port: 5432
Database Name: banking_db
Username: banking_user
Password: banking_pass
```

### Connection String
```
JDBC URL: jdbc:postgresql://localhost:5432/banking_db
PostgreSQL URL: postgresql://banking_user:banking_pass@localhost:5432/banking_db
```

---

## 📋 Schema & Tables

### Database Schema: `public` (default)

### Table: `deposits`

#### Cấu Trúc Bảng

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
```

#### Chi Tiết Các Cột

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | BIGSERIAL | PRIMARY KEY | ID tự động tăng (1, 2, 3...) |
| `account_number` | VARCHAR(50) | NOT NULL | Số tài khoản (8-20 ký tự) |
| `amount` | DECIMAL(19,2) | NOT NULL | Số tiền (tối đa 17 chữ số, 2 số thập phân) |
| `status` | VARCHAR(20) | NOT NULL | Trạng thái: PENDING, COMPLETED, FAILED, CANCELLED |
| `currency` | VARCHAR(3) | NOT NULL | Mã tiền tệ ISO (USD, VND, EUR...) |
| `description` | VARCHAR(500) | NULL | Mô tả (tùy chọn, tối đa 500 ký tự) |
| `created_at` | TIMESTAMP | NOT NULL | Thời gian tạo (tự động) |
| `updated_at` | TIMESTAMP | NOT NULL | Thời gian cập nhật (tự động) |

#### Sequence (Auto-increment)

```sql
CREATE SEQUENCE deposit_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO CYCLE;

-- Sử dụng trong table:
-- id DEFAULT nextval('deposit_sequence')
```

#### Indexes

```sql
-- Index cho tìm kiếm theo account_number (nhanh hơn)
CREATE INDEX idx_deposits_account_number ON deposits(account_number);

-- Index cho tìm kiếm theo status
CREATE INDEX idx_deposits_status ON deposits(status);

-- Index cho sắp xếp theo thời gian tạo
CREATE INDEX idx_deposits_created_at ON deposits(created_at);
```

#### Constraints

```sql
-- Primary Key
PRIMARY KEY (id)

-- Not Null Constraints
NOT NULL: id, account_number, amount, status, currency, created_at, updated_at

-- Check Constraints (nếu muốn thêm)
-- CHECK (amount > 0)
-- CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED'))
-- CHECK (LENGTH(currency) = 3)
```

---

## 📦 Dữ Liệu Mẫu

### Ví Dụ Dữ Liệu

```sql
INSERT INTO deposits (account_number, amount, status, currency, description, created_at, updated_at)
VALUES 
    ('ACC123456789', 1000.00, 'PENDING', 'USD', 'Initial deposit', NOW(), NOW()),
    ('ACC987654321', 500.50, 'COMPLETED', 'EUR', 'Monthly deposit', NOW(), NOW()),
    ('ACC111222333', 2000.00, 'FAILED', 'VND', 'Deposit attempt', NOW(), NOW());
```

### Dữ Liệu Trong Bảng

```
id | account_number | amount    | status    | currency | description       | created_at          | updated_at
---|----------------|-----------|-----------|----------|-------------------|---------------------|-------------------
1  | ACC123456789   | 1000.00   | PENDING   | USD      | Initial deposit   | 2025-10-27 10:30:00 | 2025-10-27 10:30:00
2  | ACC987654321   | 500.50    | COMPLETED | EUR      | Monthly deposit   | 2025-10-27 11:00:00 | 2025-10-27 11:05:00
3  | ACC111222333   | 2000.00   | FAILED    | VND      | Deposit attempt   | 2025-10-27 12:00:00 | 2025-10-27 12:00:00
```

---

## 🔧 Công Cụ Quản Trị Database

### 1. pgAdmin (Khuyến nghị cho PostgreSQL)

#### Cài Đặt
```bash
# Ubuntu/Debian
sudo apt-get install pgadmin4

# Hoặc download từ: https://www.pgadmin.org/download/
```

#### Kết Nối

1. **Mở pgAdmin**
2. **Add New Server:**
   - Name: `Banking Deposit DB`
   - **Connection Tab:**
     - Host: `localhost`
     - Port: `5432`
     - Database: `banking_db`
     - Username: `banking_user`
     - Password: `banking_pass`
     - Save password: ✓
   - Click **Save**

3. **Mở Database:**
   - Expand: Servers → Banking Deposit DB → Databases → banking_db → Schemas → public → Tables
   - Click vào `deposits` để xem dữ liệu

#### SQL Query Editor

```sql
-- Xem tất cả deposits
SELECT * FROM deposits;

-- Xem deposits theo status
SELECT * FROM deposits WHERE status = 'PENDING';

-- Xem deposits theo account
SELECT * FROM deposits WHERE account_number = 'ACC123456789';

-- Thống kê
SELECT 
    status,
    COUNT(*) as count,
    SUM(amount) as total_amount
FROM deposits
GROUP BY status;
```

---

### 2. DBeaver (Universal Database Tool)

#### Cài Đặt
```bash
# Download từ: https://dbeaver.io/download/
# Hoặc:
sudo snap install dbeaver-ce
```

#### Kết Nối

1. **Mở DBeaver**
2. **New Database Connection:**
   - Chọn: PostgreSQL
   - **Main Tab:**
     - Host: `localhost`
     - Port: `5432`
     - Database: `banking_db`
     - Username: `banking_user`
     - Password: `banking_pass`
   - Click **Test Connection** (sẽ hỏi download driver)
   - Click **Finish**

3. **Browse Tables:**
   - Expand: Databases → banking_db → Schemas → public → Tables
   - Double-click `deposits` để mở data editor

---

### 3. DataGrip (JetBrains)

#### Cài Đặt
- Download: https://www.jetbrains.com/datagrip/
- Cần license (hoặc 30-day trial)

#### Kết Nối

1. **File → New → Data Source → PostgreSQL**
2. **Connection:**
   - Host: `localhost`
   - Port: `5432`
   - Database: `banking_db`
   - User: `banking_user`
   - Password: `banking_pass`
3. **Test Connection → OK**
4. **Browse Database:** Left panel → banking_db → public → deposits

---

### 4. Command Line (psql)

#### Kết Nối

```bash
# Cài đặt psql (nếu chưa có)
sudo apt-get install postgresql-client

# Kết nối
psql -h localhost -p 5432 -U banking_user -d banking_db

# Hoặc từ Docker
docker exec -it banking-postgres psql -U banking_user -d banking_db
```

#### Các Lệnh Thường Dùng

```sql
-- Xem tất cả tables
\dt

-- Xem cấu trúc table
\d deposits

-- Xem tất cả databases
\l

-- Xem tất cả schemas
\dn

-- Xem indexes
\di

-- Thoát
\q

-- Execute SQL
SELECT * FROM deposits;
```

---

### 5. TablePlus (Mac/Windows/Linux)

#### Cài Đặt
- Download: https://tableplus.com/

#### Kết Nối

1. **Create New Connection → PostgreSQL**
2. **Connection:**
   - Name: `Banking DB`
   - Host: `localhost`
   - Port: `5432`
   - User: `banking_user`
   - Password: `banking_pass`
   - Database: `banking_db`
3. **Connect**
4. Browse tables và query dữ liệu

---

## 🔍 Truy Vấn SQL Thường Dùng

### Basic Queries

```sql
-- 1. Xem tất cả deposits
SELECT * FROM deposits
ORDER BY created_at DESC;

-- 2. Xem deposits theo ID
SELECT * FROM deposits
WHERE id = 1;

-- 3. Xem deposits theo account number
SELECT * FROM deposits
WHERE account_number = 'ACC123456789'
ORDER BY created_at DESC;

-- 4. Xem deposits theo status
SELECT * FROM deposits
WHERE status = 'PENDING';

-- 5. Đếm số deposits
SELECT COUNT(*) as total_deposits FROM deposits;

-- 6. Tổng số tiền theo status
SELECT 
    status,
    COUNT(*) as count,
    SUM(amount) as total_amount,
    AVG(amount) as avg_amount
FROM deposits
GROUP BY status;

-- 7. Top 10 deposits lớn nhất
SELECT * FROM deposits
ORDER BY amount DESC
LIMIT 10;

-- 8. Deposits trong ngày hôm nay
SELECT * FROM deposits
WHERE DATE(created_at) = CURRENT_DATE;

-- 9. Thống kê theo currency
SELECT 
    currency,
    COUNT(*) as count,
    SUM(amount) as total
FROM deposits
GROUP BY currency;
```

### Advanced Queries

```sql
-- Deposits theo tháng
SELECT 
    DATE_TRUNC('month', created_at) as month,
    COUNT(*) as count,
    SUM(amount) as total_amount
FROM deposits
GROUP BY month
ORDER BY month DESC;

-- Deposits chưa hoàn thành (PENDING hoặc FAILED)
SELECT * FROM deposits
WHERE status IN ('PENDING', 'FAILED')
ORDER BY created_at DESC;

-- Tìm deposits của một account trong khoảng thời gian
SELECT * FROM deposits
WHERE account_number = 'ACC123456789'
  AND created_at BETWEEN '2025-10-01' AND '2025-10-31'
ORDER BY created_at DESC;
```

---

## 🔧 Kết Nối Từ Docker Container

### Nếu Database chạy trong Docker

```bash
# Cách 1: Kết nối từ trong container
docker exec -it banking-postgres psql -U banking_user -d banking_db

# Cách 2: Kết nối từ host machine
# Host: localhost
# Port: 5432 (exposed port)
psql -h localhost -p 5432 -U banking_user -d banking_db

# Cách 3: Dùng connection string
psql postgresql://banking_user:banking_pass@localhost:5432/banking_db
```

---

## 📊 ERD (Entity Relationship Diagram)

### Mô Hình Dữ Liệu Hiện Tại

```
┌─────────────────────┐
│     deposits        │
├─────────────────────┤
│ PK id (BIGSERIAL)   │
│    account_number   │──► Index
│    amount           │
│    status           │──► Index
│    currency         │
│    description      │
│    created_at       │──► Index
│    updated_at       │
└─────────────────────┘

Sequence:
┌─────────────────────┐
│ deposit_sequence    │
├─────────────────────┤
│ Next value: auto    │
└─────────────────────┘
```

### Lưu Ý
- Hiện tại chỉ có 1 bảng (deposits)
- Có thể mở rộng thêm: accounts, transactions, customers...

---

## 🔐 Bảo Mật & Quyền

### Tạo User Mới (Nếu Cần)

```sql
-- Connect as superuser
psql -U postgres

-- Tạo user mới
CREATE USER app_user WITH PASSWORD 'secure_password';

-- Cấp quyền
GRANT CONNECT ON DATABASE banking_db TO app_user;
GRANT USAGE ON SCHEMA public TO app_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON deposits TO app_user;
GRANT USAGE, SELECT ON SEQUENCE deposit_sequence TO app_user;
```

---

## 📈 Monitoring & Maintenance

### Kiểm Tra Kích Thước Database

```sql
-- Kích thước database
SELECT pg_size_pretty(pg_database_size('banking_db')) as db_size;

-- Kích thước table
SELECT pg_size_pretty(pg_total_relation_size('deposits')) as table_size;

-- Số rows
SELECT COUNT(*) FROM deposits;
```

### Backup & Restore

```bash
# Backup
pg_dump -h localhost -U banking_user -d banking_db > backup.sql

# Restore
psql -h localhost -U banking_user -d banking_db < backup.sql

# Hoặc từ Docker
docker exec banking-postgres pg_dump -U banking_user banking_db > backup.sql
```

---

## 🎯 Tóm Tắt

### Thông Tin Kết Nối

```
Host: localhost
Port: 5432
Database: banking_db
Username: banking_user
Password: banking_pass
```

### Công Cụ Khuyến Nghị

1. **pgAdmin** - Chuyên cho PostgreSQL
2. **DBeaver** - Universal, miễn phí
3. **psql** - Command line, nhanh gọn
4. **TablePlus** - UI đẹp, dễ dùng

### Table Chính

- **deposits** - 8 columns
- **3 indexes** - Tối ưu tìm kiếm
- **1 sequence** - Auto-increment ID

**Bạn đã sẵn sàng để quản lý database! 🗄️**

