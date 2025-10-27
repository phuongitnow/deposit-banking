# Hướng Dẫn Push Code Lên GitHub

## Bước 1: Login GitHub qua Git

### Cách 1: Sử dụng Personal Access Token (Khuyến nghị)

1. **Tạo Personal Access Token trên GitHub:**
   - Vào GitHub.com → Settings → Developer settings → Personal access tokens → Tokens (classic)
   - Click "Generate new token (classic)"
   - Đặt tên token (ví dụ: "deposit-banking")
   - Chọn quyền: **repo** (Full control of private repositories)
   - Click "Generate token"
   - **Copy token ngay** (chỉ hiện 1 lần!)

2. **Login với token:**
```bash
# Git sẽ hỏi username và password
# Username: tên GitHub của bạn
# Password: paste token vừa tạo (KHÔNG phải password GitHub)
git push
```

Hoặc set token trực tiếp:
```bash
# Set username
git config user.name "your-github-username"

# Set email
git config user.email "your-email@example.com"
```

### Cách 2: Sử dụng SSH Key

1. **Tạo SSH key:**
```bash
ssh-keygen -t ed25519 -C "your-email@example.com"
# Nhấn Enter để chấp nhận default location
# Nhập passphrase (có thể để trống)
```

2. **Copy public key:**
```bash
cat ~/.ssh/id_ed25519.pub
# Copy toàn bộ output
```

3. **Thêm vào GitHub:**
   - GitHub.com → Settings → SSH and GPG keys
   - Click "New SSH key"
   - Paste public key vào

4. **Test connection:**
```bash
ssh -T git@github.com
# Sẽ hiện: "Hi username! You've successfully authenticated..."
```

## Bước 2: Tạo Repository trên GitHub

1. Đăng nhập GitHub.com
2. Click nút **"+"** → **"New repository"**
3. Đặt tên: `deposit-banking`
4. Chọn **Public** hoặc **Private**
5. **KHÔNG** tích "Initialize with README"
6. Click **"Create repository"**
7. GitHub sẽ hiển thị URL, copy URL đó (ví dụ: `https://github.com/username/deposit-banking.git`)

## Bước 3: Push Code Lên GitHub

```bash
# 1. Khởi tạo git (nếu chưa có)
git init

# 2. Add remote repository
git remote add origin https://github.com/your-username/deposit-banking.git

# 3. Add tất cả files
git add .

# 4. Commit
git commit -m "Initial commit: Banking Deposit API with Spring Boot 3, Docker, PostgreSQL"

# 5. Set branch chính
git branch -M main

# 6. Push lên GitHub
git push -u origin main

# Nếu gặp lỗi authentication:
# - Username: tên GitHub của bạn
# - Password: Personal Access Token (KHÔNG phải password)
```

## Bước 4: Lần sau push code

```bash
# Chỉ cần 3 lệnh này:
git add .
git commit -m "Update: mô tả thay đổi"
git push
```

## Lưu ý

⚠️ **Personal Access Token** là cách an toàn nhất thay vì dùng password GitHub.

🔑 **SSH Key** tiện lợi hơn vì không cần nhập token mỗi lần.

📁 **.gitignore** đã được tạo sẵn để không commit:
- Build files
- IDE files  
- Logs
- Docker volumes

## Quick Commands

```bash
# Kiểm tra git status
git status

# Xem log commit
git log --oneline

# Undo last commit (giữ thay đổi)
git reset --soft HEAD~1

# Xem remote
git remote -v
```

## Giải Quyết Lỗi

### Lỗi: "Authentication failed"
- Kiểm tra lại Personal Access Token
- Đảm bảo token chưa hết hạn
- Tạo token mới nếu cần

### Lỗi: "Repository not found"
- Kiểm tra username/repository name
- Đảm bảo repo tồn tại trên GitHub
- Kiểm tra quyền truy cập

### Lỗi: "Updates were rejected"
```bash
# Pull code mới nhất trước
git pull origin main --rebase
# Sau đó push lại
git push
```

## Tổng Kết

Sau khi push thành công, bạn có thể:
- ✅ View code trên GitHub
- ✅ Share repository với team
- ✅ Clone về máy khác
- ✅ Xem lịch sử commit
- ✅ Tạo issues và pull requests

**Happy Coding! 🚀**

