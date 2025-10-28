# 📊 Phân Tích Nghiệp Vụ - Banking Deposit API

*Vai trò: Business Analyst*  
*Mục tiêu: Giải thích nghiệp vụ một cách dễ hiểu, rõ ràng*

---

## 🎯 TỔNG QUAN NGHIỆP VỤ

### Vấn Đề Cần Giải Quyết
**Ngân hàng cần một hệ thống quản lý giao dịch tiền gửi của khách hàng.**

### Giải Pháp
**Xây dựng API cho phép nhân viên ngân hàng:**
- Ghi nhận giao dịch tiền gửi
- Theo dõi trạng thái giao dịch
- Tra cứu lịch sử giao dịch
- Cập nhật trạng thái giao dịch
- Xóa/cancel giao dịch không hợp lệ

---

## 📝 NGHIỆP VỤ CHI TIẾT

### 1️⃣ NGHIỆP VỤ: TẠO GIAO DỊCH TIỀN GỬI MỚI

**Mô tả:** Khi khách hàng đến nộp tiền vào tài khoản, nhân viên ngân hàng cần ghi nhận giao dịch vào hệ thống.

**Quy trình nghiệp vụ:**
```
1. Khách hàng đưa tiền + phiếu gửi tiền
   ↓
2. Nhân viên kiểm tra:
   ✅ Số tài khoản hợp lệ (8-20 ký tự, chữ hoa + số)
   ✅ Số tiền > 0
   ✅ Đơn vị tiền tệ đúng (USD, VND, EUR...)
   ↓
3. Nhân viên nhập vào hệ thống:
   - Số tài khoản
   - Số tiền
   - Loại tiền tệ
   - Ghi chú (nếu có)
   ↓
4. Hệ thống tự động:
   ✅ Tạo ID duy nhất
   ✅ Set trạng thái = PENDING (Đang chờ xử lý)
   ✅ Ghi thời gian tạo
   ✅ Trả về thông tin giao dịch
```

**Dữ liệu cần thiết:**
- **Số tài khoản** (accountNumber): Bắt buộc, 8-20 ký tự
- **Số tiền** (amount): Bắt buộc, phải > 0
- **Đơn vị tiền tệ** (currency): Bắt buộc, 3 ký tự (USD, VND...)
- **Mô tả** (description): Không bắt buộc, tối đa 500 ký tự

**Kết quả:**
- Hệ thống tạo giao dịch với trạng thái "PENDING"
- Trả về ID giao dịch để nhân viên tra cứu sau

---

### 2️⃣ NGHIỆP VỤ: TRA CỨU GIAO DỊCH

**a) Tra cứu theo ID**

**Mô tả:** Nhân viên cần tra cứu thông tin chi tiết một giao dịch cụ thể.

**Quy trình:**
```
1. Nhân viên nhập ID giao dịch
   ↓
2. Hệ thống tìm kiếm trong database
   ↓
3. Nếu TÌM THẤY:
   ✅ Trả về đầy đủ thông tin giao dịch
   ✅ Bao gồm: số tài khoản, số tiền, trạng thái, thời gian...
   
   Nếu KHÔNG TÌM THẤY:
   ❌ Trả về lỗi: "Giao dịch không tồn tại"
```

**b) Xem danh sách giao dịch**

**Mô tả:** Nhân viên muốn xem tất cả hoặc một phần giao dịch (có phân trang).

**Quy trình:**
```
1. Nhân viên yêu cầu danh sách
   ↓
2. Hệ thống hiển thị:
   - Danh sách giao dịch (mặc định 20 giao dịch/trang)
   - Sắp xếp theo thời gian tạo (mới nhất trước)
   - Thông tin phân trang (tổng số, số trang...)
   ↓
3. Nhân viên có thể:
   - Xem trang tiếp theo
   - Thay đổi số giao dịch/trang
   - Sắp xếp theo tiêu chí khác (ví dụ: theo số tiền)
```

**c) Xem giao dịch theo tài khoản**

**Mô tả:** Nhân viên muốn xem tất cả giao dịch của một khách hàng cụ thể.

**Quy trình:**
```
1. Nhân viên nhập số tài khoản
   ↓
2. Hệ thống tìm tất cả giao dịch của tài khoản đó
   ↓
3. Hiển thị danh sách có phân trang
   ✅ Tính năng hữu ích khi khách hàng muốn xem lịch sử
```

---

### 3️⃣ NGHIỆP VỤ: CẬP NHẬT TRẠNG THÁI GIAO DỊCH

**Mô tả:** Giao dịch thay đổi trạng thái trong quy trình xử lý.

**Các trạng thái:**
1. **PENDING** (Đang chờ) - Mới tạo, chờ xử lý
2. **COMPLETED** (Hoàn thành) - Đã xử lý thành công
3. **FAILED** (Thất bại) - Xử lý không thành công
4. **CANCELLED** (Đã hủy) - Khách hàng/thao tác hủy bỏ

**Quy trình:**
```
Một giao dịch thường trải qua các bước:

[PENDING] → [COMPLETED]
              ↓
           [FAILED] hoặc [CANCELLED]

Ví dụ quy trình thực tế:
1. Khách hàng nộp tiền → PENDING
2. Nhân viên xác nhận → COMPLETED
3. Nếu có vấn đề → FAILED
4. Nếu khách hàng thay đổi ý định → CANCELLED
```

**Cập nhật trạng thái:**
```
1. Nhân viên xác định giao dịch cần cập nhật (theo ID)
   ↓
2. Chọn trạng thái mới
   ↓
3. Hệ thống cập nhật:
   ✅ Thay đổi status
   ✅ Ghi nhận thời gian cập nhật
   ✅ Kiểm tra hợp lệ của trạng thái mới
   ↓
4. Trả về thông tin giao dịch đã cập nhật
```

---

### 4️⃣ NGHIỆP VỤ: XÓA/HỦY GIAO DỊCH

**Mô tả:** Nhân viên cần xóa giao dịch không hợp lệ hoặc bị nhầm.

**Trường hợp sử dụng:**
- Nhập nhầm thông tin giao dịch
- Khách hàng hủy giao dịch
- Giao dịch duplicate (trùng lặp)
- Giao dịch test/kiểm thử

**Quy trình:**
```
1. Nhân viên xác định giao dịch cần xóa (theo ID)
   ↓
2. Hệ thống kiểm tra:
   ✅ Giao dịch có tồn tại không?
   ↓
3. Nếu TỒN TẠI:
   ✅ Xóa khỏi database
   ✅ Trả về xác nhận đã xóa
   
   Nếu KHÔNG TỒN TẠI:
   ❌ Trả về lỗi: "Giao dịch không tồn tại"
```

**Lưu ý nghiệp vụ:**
- ⚠️ Nên có quyền phê duyệt trước khi xóa giao dịch đã hoàn thành
- ⚠️ Ghi log để audit (đã có trong hệ thống)

---

## 🔄 LUỒNG NGHIỆP VỤ TỔNG THỂ

### Scenario 1: Quy trình chuẩn
```
1. Khách hàng đến nộp tiền
   ↓
2. Nhân viên tạo giao dịch → Status: PENDING
   ↓
3. Nhân viên xác nhận → Status: COMPLETED
   ↓
4. Giao dịch hoàn tất
```

### Scenario 2: Quy trình có vấn đề
```
1. Khách hàng đến nộp tiền
   ↓
2. Nhân viên tạo giao dịch → Status: PENDING
   ↓
3. Phát hiện vấn đề (tiền không đủ, thông tin sai...) 
   ↓
4a. Khắc phục được → Status: COMPLETED
4b. Không khắc phục được → Status: FAILED
```

### Scenario 3: Khách hàng hủy
```
1. Khách hàng đến nộp tiền
   ↓
2. Nhân viên tạo giao dịch → Status: PENDING
   ↓
3. Khách hàng thay đổi ý định
   ↓
4. Nhân viên hủy giao dịch → Status: CANCELLED
```

---

## 🎯 ĐỐI TƯỢNG SỬ DỤNG

### 1. Nhân Viên Ngân Hàng (Front-end user)
- **Mục đích**: Xử lý giao dịch gửi tiền của khách hàng
- **Nhu cầu**: 
  - Tạo giao dịch nhanh chóng
  - Tra cứu thông tin khách hàng
  - Cập nhật trạng thái giao dịch
- **Kỳ vọng**: Giao diện đơn giản, thao tác nhanh

### 2. Quản Lý Ngân Hàng (Manager)
- **Mục đích**: Quản lý, giám sát giao dịch
- **Nhu cầu**:
  - Xem báo cáo tổng hợp
  - Phân tích số liệu
  - Audit trail (lịch sử thay đổi)
- **Kỳ vọng**: Dữ liệu đầy đủ, chính xác, có thể export

### 3. Khách Hàng
- **Mục đích**: Kiểm tra lịch sử giao dịch
- **Nhu cầu**:
  - Xem giao dịch của mình
  - Xác nhận số dư
- **Kỳ vọng**: Dữ liệu chính xác, bảo mật

---

## 📊 DỮ LIỆU LƯU TRỮ

### Thông Tin Giao Dịch (Deposit)

```
┌─────────────────────────────────────────┐
│           GIAO DỊCH TIỀN GỬI             │
├─────────────────────────────────────────┤
│ ID (tự động tạo)                        │
│ Số tài khoản (8-20 ký tự)              │
│ Số tiền (> 0)                          │
│ Đơn vị tiền tệ (USD, VND...)           │
│ Trạng thái (PENDING, COMPLETED...)     │
│ Mô tả (tùy chọn)                       │
│ Thời gian tạo                           │
│ Thời gian cập nhật                      │
└─────────────────────────────────────────┘
```

**Ví dụ thực tế:**
```json
{
  "id": 12345,
  "accountNumber": "ACC123456789",
  "amount": 500000.00,
  "status": "COMPLETED",
  "currency": "VND",
  "description": "Nộp tiền tháng 10",
  "createdAt": "2025-10-27 09:30:00",
  "updatedAt": "2025-10-27 09:35:00"
}
```

---

## ✅ RÀNG BUỘC NGHIỆP VỤ (Business Rules)

### 1. Ràng buộc dữ liệu
- ✅ Số tài khoản: Bắt buộc, 8-20 ký tự
- ✅ Số tiền: Bắt buộc, phải > 0
- ✅ Đơn vị tiền tệ: Bắt buộc, phải đúng format ISO
- ✅ Mô tả: Không bắt buộc, tối đa 500 ký tự

### 2. Ràng buộc trạng thái
- ✅ Trạng thái mặc định khi tạo: PENDING
- ✅ Chỉ có 4 trạng thái hợp lệ: PENDING, COMPLETED, FAILED, CANCELLED
- ✅ Không thể chuyển trạng thái về PENDING từ các trạng thái khác

### 3. Ràng buộc về thời gian
- ✅ Không thể chỉnh sửa giao dịch đã COMPLETED hoặc FAILED
- ✅ Thời gian tạo và cập nhật được ghi nhận tự động

---

## 🎯 LỢI ÍCH CỦA HỆ THỐNG

### Cho Nhân Viên
✅ Tạo giao dịch nhanh chóng, chính xác
✅ Tra cứu thông tin dễ dàng
✅ Giảm sai sót nhờ validation tự động
✅ Quản lý trạng thái giao dịch rõ ràng

### Cho Quản Lý
✅ Có đầy đủ dữ liệu để báo cáo
✅ Theo dõi hiệu suất giao dịch
✅ Audit trail đầy đủ
✅ Phân tích xu hướng

### Cho Ngân Hàng
✅ Giảm thời gian xử lý
✅ Tăng độ chính xác dữ liệu
✅ Dễ dàng mở rộng
✅ Tuân thủ quy định (có lịch sử đầy đủ)

---

## 📈 METRICS ĐO LƯỜNG

### Định lượng
- Số giao dịch/ngày, tháng
- Số tiền gửi trung bình
- Tỷ lệ giao dịch thành công (COMPLETED)
- Tỷ lệ giao dịch thất bại (FAILED)

### Định tính
- Thời gian xử lý giao dịch
- Độ chính xác dữ liệu
- Mức độ hài lòng của nhân viên
- Hiệu quả tra cứu

---

## 🔍 KỊCH BẢN SỬ DỤNG (Use Cases)

### Use Case 1: Xử lý nộp tiền mặt
**Actor:** Nhân viên ngân hàng  
**Goal:** Ghi nhận giao dịch nộp tiền của khách hàng

**Steps:**
1. Khách hàng đưa tiền và phiếu gửi tiền
2. Nhân viên nhập thông tin vào hệ thống
3. Hệ thống validate thông tin
4. Hệ thống tạo giao dịch với ID
5. Nhân viên xác nhận với khách hàng
6. Nhân viên cập nhật trạng thái COMPLETED

### Use Case 2: Tra cứu lịch sử khách hàng
**Actor:** Nhân viên ngân hàng  
**Goal:** Xem lịch sử giao dịch của khách hàng

**Steps:**
1. Nhân viên nhập số tài khoản
2. Hệ thống hiển thị danh sách giao dịch
3. Nhân viên xem chi tiết nếu cần
4. Báo cáo cho khách hàng

---

## 💡 TÓM TẮT

**Hệ thống này giúp ngân hàng:**
1. ✅ Quản lý giao dịch tiền gửi một cách chuyên nghiệp
2. ✅ Theo dõi trạng thái giao dịch rõ ràng
3. ✅ Tra cứu thông tin nhanh chóng
4. ✅ Có dữ liệu đầy đủ để báo cáo và phân tích
5. ✅ Đảm bảo tính chính xác với validation tự động

**Đây là một phần quan trọng của hệ thống ngân hàng hiện đại!** 🏦

