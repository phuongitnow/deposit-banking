-- ============================================
-- QUICK QUERY EXAMPLES - Banking Deposit DB
-- ============================================

-- ============================================
-- 1. BASIC QUERIES
-- ============================================

-- Xem tất cả deposits
SELECT * FROM deposits ORDER BY created_at DESC;

-- Xem 10 deposits mới nhất
SELECT * FROM deposits ORDER BY created_at DESC LIMIT 10;

-- Xem deposit theo ID
SELECT * FROM deposits WHERE id = 1;

-- Đếm tổng số deposits
SELECT COUNT(*) as total_deposits FROM deposits;

-- ============================================
-- 2. QUERIES BY ACCOUNT
-- ============================================

-- Xem tất cả deposits của một account
SELECT * FROM deposits 
WHERE account_number = 'ACC123456789'
ORDER BY created_at DESC;

-- Tổng số tiền của một account
SELECT 
    account_number,
    COUNT(*) as transaction_count,
    SUM(amount) as total_amount,
    SUM(CASE WHEN status = 'COMPLETED' THEN amount ELSE 0 END) as completed_amount
FROM deposits
WHERE account_number = 'ACC123456789'
GROUP BY account_number;

-- ============================================
-- 3. QUERIES BY STATUS
-- ============================================

-- Deposits đang chờ xử lý
SELECT * FROM deposits 
WHERE status = 'PENDING'
ORDER BY created_at ASC;

-- Deposits đã hoàn thành
SELECT * FROM deposits 
WHERE status = 'COMPLETED'
ORDER BY created_at DESC;

-- Deposits thất bại
SELECT * FROM deposits 
WHERE status = 'FAILED';

-- Deposits đã hủy
SELECT * FROM deposits 
WHERE status = 'CANCELLED';

-- Deposits chưa hoàn thành (PENDING hoặc FAILED)
SELECT * FROM deposits 
WHERE status IN ('PENDING', 'FAILED')
ORDER BY created_at DESC;

-- ============================================
-- 4. STATISTICS & REPORTS
-- ============================================

-- Thống kê theo status
SELECT 
    status,
    COUNT(*) as count,
    SUM(amount) as total_amount,
    AVG(amount) as avg_amount,
    MIN(amount) as min_amount,
    MAX(amount) as max_amount
FROM deposits
GROUP BY status
ORDER BY count DESC;

-- Thống kê theo currency
SELECT 
    currency,
    COUNT(*) as count,
    SUM(amount) as total_amount,
    AVG(amount) as avg_amount
FROM deposits
GROUP BY currency
ORDER BY total_amount DESC;

-- Top 10 deposits lớn nhất
SELECT 
    id,
    account_number,
    amount,
    currency,
    status,
    created_at
FROM deposits
ORDER BY amount DESC
LIMIT 10;

-- Thống kê theo account
SELECT 
    account_number,
    COUNT(*) as transaction_count,
    SUM(amount) as total_amount,
    COUNT(CASE WHEN status = 'COMPLETED' THEN 1 END) as completed_count,
    COUNT(CASE WHEN status = 'PENDING' THEN 1 END) as pending_count,
    MAX(created_at) as last_transaction
FROM deposits
GROUP BY account_number
ORDER BY total_amount DESC;

-- ============================================
-- 5. TIME-BASED QUERIES
-- ============================================

-- Deposits hôm nay
SELECT * FROM deposits 
WHERE DATE(created_at) = CURRENT_DATE
ORDER BY created_at DESC;

-- Deposits trong 7 ngày qua
SELECT * FROM deposits 
WHERE created_at >= CURRENT_DATE - INTERVAL '7 days'
ORDER BY created_at DESC;

-- Deposits trong tháng hiện tại
SELECT * FROM deposits 
WHERE DATE_TRUNC('month', created_at) = DATE_TRUNC('month', CURRENT_DATE)
ORDER BY created_at DESC;

-- Thống kê theo ngày
SELECT 
    DATE(created_at) as date,
    COUNT(*) as count,
    SUM(amount) as total_amount
FROM deposits
GROUP BY DATE(created_at)
ORDER BY date DESC
LIMIT 30;

-- Thống kê theo tháng
SELECT 
    DATE_TRUNC('month', created_at) as month,
    COUNT(*) as count,
    SUM(amount) as total_amount,
    AVG(amount) as avg_amount
FROM deposits
GROUP BY DATE_TRUNC('month', created_at)
ORDER BY month DESC;

-- ============================================
-- 6. SEARCH & FILTER
-- ============================================

-- Tìm kiếm theo account (partial match)
SELECT * FROM deposits 
WHERE account_number LIKE 'ACC%'
ORDER BY created_at DESC;

-- Deposits với amount lớn hơn một giá trị
SELECT * FROM deposits 
WHERE amount > 1000.00
ORDER BY amount DESC;

-- Deposits trong khoảng amount
SELECT * FROM deposits 
WHERE amount BETWEEN 500.00 AND 2000.00
ORDER BY amount DESC;

-- Deposits theo account và status
SELECT * FROM deposits 
WHERE account_number = 'ACC123456789'
  AND status = 'PENDING'
ORDER BY created_at DESC;

-- Deposits trong khoảng thời gian
SELECT * FROM deposits 
WHERE created_at BETWEEN '2025-10-01 00:00:00' AND '2025-10-31 23:59:59'
ORDER BY created_at DESC;

-- ============================================
-- 7. DATA QUALITY CHECKS
-- ============================================

-- Kiểm tra deposits có description null
SELECT * FROM deposits 
WHERE description IS NULL;

-- Kiểm tra deposits có amount = 0 hoặc âm
SELECT * FROM deposits 
WHERE amount <= 0;

-- Kiểm tra deposits có currency không đúng format
SELECT * FROM deposits 
WHERE LENGTH(currency) != 3;

-- Kiểm tra deposits có account_number không hợp lệ
SELECT * FROM deposits 
WHERE LENGTH(account_number) < 8 OR LENGTH(account_number) > 20;

-- ============================================
-- 8. ADVANCED ANALYTICS
-- ============================================

-- Success rate (tỷ lệ thành công)
SELECT 
    COUNT(*) as total,
    COUNT(CASE WHEN status = 'COMPLETED' THEN 1 END) as completed,
    COUNT(CASE WHEN status = 'FAILED' THEN 1 END) as failed,
    ROUND(100.0 * COUNT(CASE WHEN status = 'COMPLETED' THEN 1 END) / COUNT(*), 2) as success_rate_percent
FROM deposits;

-- Average transaction amount by status
SELECT 
    status,
    ROUND(AVG(amount), 2) as avg_amount,
    ROUND(SUM(amount) / COUNT(*), 2) as avg_total
FROM deposits
GROUP BY status;

-- Growth trend (số lượng deposits theo ngày)
SELECT 
    DATE(created_at) as date,
    COUNT(*) as daily_count,
    SUM(COUNT(*)) OVER (ORDER BY DATE(created_at)) as cumulative_count
FROM deposits
GROUP BY DATE(created_at)
ORDER BY date DESC;

-- ============================================
-- 9. MAINTENANCE QUERIES
-- ============================================

-- Xóa deposits test (cẩn thận!)
-- DELETE FROM deposits WHERE description LIKE 'Test%';

-- Reset sequence về 1
-- ALTER SEQUENCE deposit_sequence RESTART WITH 1;

-- Xem kích thước table
SELECT pg_size_pretty(pg_total_relation_size('deposits')) as table_size;

-- Xem thống kê table
SELECT 
    schemaname,
    tablename,
    n_tup_ins as inserts,
    n_tup_upd as updates,
    n_tup_del as deletes
FROM pg_stat_user_tables
WHERE tablename = 'deposits';

-- ============================================
-- 10. EXPORT DATA
-- ============================================

-- Export to CSV (chạy trong psql)
-- \copy deposits TO 'deposits_export.csv' CSV HEADER;

-- Export deposits của một account
-- \copy (SELECT * FROM deposits WHERE account_number = 'ACC123456789') TO 'account_deposits.csv' CSV HEADER;

