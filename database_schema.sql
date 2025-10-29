-- ============================================
-- Banking Deposit Database Schema
-- PostgreSQL 16
-- ============================================

-- Database: banking_db
-- User: banking_user

-- ============================================
-- SEQUENCE: Auto-increment ID
-- ============================================
CREATE SEQUENCE IF NOT EXISTS deposit_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO CYCLE
    OWNED BY deposits.id;

-- ============================================
-- TABLE: deposits
-- ============================================
CREATE TABLE IF NOT EXISTS deposits (
    id BIGSERIAL PRIMARY KEY DEFAULT nextval('deposit_sequence'),
    account_number VARCHAR(50) NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- INDEXES: Tối ưu tìm kiếm
-- ============================================

-- Index cho tìm kiếm theo account_number
CREATE INDEX IF NOT EXISTS idx_deposits_account_number 
ON deposits(account_number);

-- Index cho tìm kiếm theo status
CREATE INDEX IF NOT EXISTS idx_deposits_status 
ON deposits(status);

-- Index cho sắp xếp theo thời gian tạo
CREATE INDEX IF NOT EXISTS idx_deposits_created_at 
ON deposits(created_at);

-- Composite index cho tìm kiếm theo account + status
CREATE INDEX IF NOT EXISTS idx_deposits_account_status 
ON deposits(account_number, status);

-- ============================================
-- SAMPLE DATA: Dữ liệu mẫu để test
-- ============================================
INSERT INTO deposits (account_number, amount, status, currency, description, created_at, updated_at)
VALUES 
    ('ACC123456789', 1000.00, 'PENDING', 'USD', 'Initial deposit', NOW(), NOW()),
    ('ACC987654321', 500.50, 'COMPLETED', 'EUR', 'Monthly deposit', NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day'),
    ('ACC111222333', 2000.00, 'FAILED', 'VND', 'Deposit attempt', NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days'),
    ('ACC123456789', 1500.75, 'COMPLETED', 'USD', 'Second deposit', NOW() - INTERVAL '3 hours', NOW() - INTERVAL '3 hours'),
    ('ACC999888777', 750.25, 'PENDING', 'GBP', 'New account deposit', NOW() - INTERVAL '5 hours', NOW() - INTERVAL '5 hours')
ON CONFLICT DO NOTHING;

-- ============================================
-- QUERIES: Các query thường dùng
-- ============================================

-- 1. Xem tất cả deposits
-- SELECT * FROM deposits ORDER BY created_at DESC;

-- 2. Xem deposits theo account
-- SELECT * FROM deposits WHERE account_number = 'ACC123456789';

-- 3. Thống kê theo status
-- SELECT status, COUNT(*) as count, SUM(amount) as total 
-- FROM deposits GROUP BY status;

-- 4. Deposits chưa hoàn thành
-- SELECT * FROM deposits WHERE status IN ('PENDING', 'FAILED');

-- ============================================
-- VIEWS: Tạo view cho báo cáo (optional)
-- ============================================

-- View: Tổng hợp theo account
CREATE OR REPLACE VIEW v_deposits_by_account AS
SELECT 
    account_number,
    COUNT(*) as transaction_count,
    SUM(amount) as total_amount,
    MAX(created_at) as last_transaction,
    COUNT(CASE WHEN status = 'COMPLETED' THEN 1 END) as completed_count
FROM deposits
GROUP BY account_number;

-- View: Thống kê theo status
CREATE OR REPLACE VIEW v_deposits_status_summary AS
SELECT 
    status,
    COUNT(*) as count,
    SUM(amount) as total_amount,
    AVG(amount) as avg_amount,
    MIN(amount) as min_amount,
    MAX(amount) as max_amount
FROM deposits
GROUP BY status;

-- ============================================
-- FUNCTIONS: Helper functions (optional)
-- ============================================

-- Function: Tự động update updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Trigger: Tự động update updated_at khi update
CREATE TRIGGER update_deposits_updated_at
    BEFORE UPDATE ON deposits
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- ============================================
-- GRANTS: Cấp quyền (nếu cần tạo user mới)
-- ============================================
-- GRANT SELECT, INSERT, UPDATE, DELETE ON deposits TO banking_user;
-- GRANT USAGE, SELECT ON SEQUENCE deposit_sequence TO banking_user;

-- ============================================
-- VERIFICATION: Kiểm tra schema
-- ============================================
-- \dt                    -- Xem tables
-- \d deposits            -- Xem cấu trúc table
-- \di                    -- Xem indexes
-- SELECT COUNT(*) FROM deposits;  -- Đếm rows

-- ============================================
-- COMMENTS: Ghi chú cho các objects
-- ============================================
COMMENT ON TABLE deposits IS 'Bảng lưu trữ giao dịch tiền gửi ngân hàng';
COMMENT ON COLUMN deposits.id IS 'ID tự động tăng';
COMMENT ON COLUMN deposits.account_number IS 'Số tài khoản khách hàng (8-20 ký tự)';
COMMENT ON COLUMN deposits.amount IS 'Số tiền gửi (DECIMAL 19,2)';
COMMENT ON COLUMN deposits.status IS 'Trạng thái: PENDING, COMPLETED, FAILED, CANCELLED';
COMMENT ON COLUMN deposits.currency IS 'Mã tiền tệ ISO (3 ký tự: USD, VND, EUR...)';
COMMENT ON COLUMN deposits.description IS 'Mô tả giao dịch (tối đa 500 ký tự)';
COMMENT ON COLUMN deposits.created_at IS 'Thời gian tạo giao dịch';
COMMENT ON COLUMN deposits.updated_at IS 'Thời gian cập nhật giao dịch';

