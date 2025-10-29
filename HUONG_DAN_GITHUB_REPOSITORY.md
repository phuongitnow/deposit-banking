# 📦 Hướng Dẫn Tổ Chức Repository Trên GitHub

## 🎯 Cấu Trúc Repository Trên GitHub

### Cách GitHub Tổ Chức Dự Án

```
GitHub.com
  └── Your Username (your-username)
      └── Repositories
          └── deposit-banking (tên repository)
              ├── README.md
              ├── src/ (source code)
              ├── docs/ (documentation - nếu tách riêng)
              └── ... (các files khác)
```

---

## 📁 Cấu Trúc Thư Mục Của Project

### Tổ Chức Files Trên GitHub

```
deposit-banking/
│
├── 📄 README.md                    # File chính - hiển thị trên GitHub
├── 📄 LICENSE                       # License (tùy chọn)
│
├── 📁 src/                         # Source code
│   ├── main/
│   │   ├── java/                   # Java source files
│   │   └── resources/              # Config, migrations
│   └── test/                       # Test files
│
├── 📁 gradle/                      # Gradle wrapper
│   └── wrapper/
│
├── 📄 build.gradle                 # Build configuration
├── 📄 settings.gradle              # Project settings
├── 📄 gradlew                      # Gradle wrapper (Linux/Mac)
├── 📄 gradlew.bat                  # Gradle wrapper (Windows)
│
├── 📄 docker-compose.yml           # Docker Compose
├── 📄 Dockerfile                   # Docker build
├── 📄 .dockerignore                # Docker ignore
├── 📄 .gitignore                   # Git ignore
│
├── 📁 Documentation/               # Tất cả documentation (tùy chọn)
│   ├── API.md
│   ├── ARCHITECTURE.md
│   ├── QUICKSTART.md
│   ├── BUILD_INSTRUCTIONS.md
│   └── ...
│
└── 📄 postman_collection.json      # Postman collection
```

---

## 🚀 Bước 1: Tạo Repository Trên GitHub

### Cách 1: Tạo Trên Web GitHub

1. **Đăng nhập GitHub.com**
   - Vào https://github.com
   - Login với tài khoản của bạn

2. **Tạo Repository Mới**
   - Click nút **"+"** (góc trên phải)
   - Chọn **"New repository"**

3. **Điền Thông Tin**
   ```
   Repository name: deposit-banking
   Description: Banking Deposit API - Spring Boot 3, Java 17, Clean Architecture
   
   Visibility:
   ☐ Public    (mọi người có thể xem)
   ☑ Private   (chỉ bạn xem - khuyến nghị cho project cá nhân)
   
   ⚠️ KHÔNG tích:
   ☐ Add a README file
   ☐ Add .gitignore
   ☐ Choose a license
   ```

4. **Click "Create repository"**
   - GitHub sẽ hiển thị URL: `https://github.com/your-username/deposit-banking.git`
   - Copy URL này để dùng sau

---

## 🔧 Bước 2: Push Code Lên GitHub

### Commands Từng Bước

```bash
# 1. Vào thư mục project
cd /home/p/deposit-banking

# 2. Khởi tạo git (nếu chưa có)
git init

# 3. Add remote repository
git remote add origin https://github.com/YOUR_USERNAME/deposit-banking.git

# Thay YOUR_USERNAME bằng tên GitHub của bạn
# Ví dụ: git remote add origin https://github.com/johndoe/deposit-banking.git

# 4. Thêm tất cả files
git add .

# 5. Commit lần đầu
git commit -m "Initial commit: Banking Deposit API

- Java 17 + Spring Boot 3
- Clean Architecture
- PostgreSQL + Liquibase
- Docker support
- Complete CRUD API
- Unit tests
- Full documentation"

# 6. Set branch chính
git branch -M main

# 7. Push lên GitHub
git push -u origin main
```

### Authentication

Khi push, GitHub sẽ hỏi:
- **Username**: Tên GitHub của bạn
- **Password**: Dán **Personal Access Token** (KHÔNG phải password GitHub)

**Tạo Personal Access Token:**
1. GitHub.com → Settings → Developer settings
2. Personal access tokens → Tokens (classic)
3. Generate new token
4. Chọn quyền: **repo** (Full control)
5. Copy token (chỉ hiện 1 lần!)

---

## 📂 Files Nào Nên Push Lên GitHub?

### ✅ Nên Push (Required)

```
✅ Source code (.java files)
✅ Configuration files (.gradle, .yml, .yaml)
✅ Documentation (.md files)
✅ Docker files (Dockerfile, docker-compose.yml)
✅ Build files (build.gradle, settings.gradle)
✅ Git files (.gitignore)
✅ Test files
✅ README.md (hiển thị trên trang chủ repository)
```

### ❌ KHÔNG Nên Push (.gitignore đã loại bỏ)

```
❌ Build output (build/, .gradle/)
❌ IDE files (.idea/, .vscode/, *.iml)
❌ Logs (*.log)
❌ Database files (*.db)
❌ Compiled classes (*.class)
❌ Jar files (trừ gradle wrapper jar)
❌ OS files (.DS_Store)
```

---

## 📍 Vị Trí Files Trên GitHub

### Sau Khi Push Lên GitHub:

```
GitHub Repository: github.com/your-username/deposit-banking
│
├── 📄 README.md
│   └── Hiển thị ngay dưới repository name
│   └── Đây là file README chính của bạn
│
├── 📁 src/
│   └── Cấu trúc code như local
│
├── 📁 Documentation files
│   └── Tất cả file .md nằm ở root
│
├── 📄 build.gradle
├── 📄 docker-compose.yml
├── 📄 Dockerfile
└── ...
```

### Trên GitHub Web:

Khi vào repository, bạn sẽ thấy:
```
your-username / deposit-banking
                 Public/Private
                 
Description: Banking Deposit API...

[Code] [Issues] [Pull requests] [Actions] [Projects] [Wiki] [Security] [Insights] [Settings]

Readme.md (hiển thị ở đây)
─────────────────────────────
# Banking Deposit API
...
─────────────────────────────

📁 src
📁 gradle
📄 build.gradle
📄 docker-compose.yml
📄 README.md
📄 API.md
📄 ARCHITECTURE.md
...
```

---

## 📝 Tổ Chức Documentation

### Option 1: Để Ở Root (Khuyến nghị)

```
deposit-banking/
├── README.md                    # Main guide
├── API.md                       # API documentation
├── ARCHITECTURE.md              # Architecture guide
├── QUICKSTART.md               # Quick start
├── ... (các docs khác)
└── src/
```

**Ưu điểm:** Dễ tìm, GitHub hiển thị tốt

### Option 2: Tách Riêng Thư Mục

```
deposit-banking/
├── README.md
├── docs/
│   ├── API.md
│   ├── ARCHITECTURE.md
│   └── ...
└── src/
```

**Ưu điểm:** Tổ chức gọn hơn, nhưng cần tạo thư mục

---

## 🎯 Cấu Trúc Hoàn Chỉnh Trên GitHub

### Sau Khi Push:

```
github.com/your-username/deposit-banking
│
├── 📄 README.md (Main documentation)
├── 📄 API.md
├── 📄 ARCHITECTURE.md
├── 📄 QUICKSTART.md
├── 📄 BUILD_INSTRUCTIONS.md
├── 📄 INSTALL.md
├── 📄 HUONG_DAN_GITHUB.md
├── 📄 HUONG_DAN_TEST_POSTMAN.md
├── 📄 PHAN_TICH_NGHIEP_VU_BA.md
├── 📄 TOM_TAT_LOGIC_NGHIEP_VU.md
├── 📄 YEU_CAU_BAN_DAU.md
│
├── 📁 src/
│   ├── main/
│   │   ├── java/com/banking/deposit/
│   │   └── resources/
│   └── test/
│
├── 📁 gradle/
├── 📄 build.gradle
├── 📄 settings.gradle
├── 📄 gradlew
├── 📄 gradlew.bat
├── 📄 docker-compose.yml
├── 📄 Dockerfile
├── 📄 .gitignore
└── 📄 postman_collection.json
```

---

## 📋 Checklist Trước Khi Push

### ✅ Checklist

- [ ] Đã tạo repository trên GitHub
- [ ] Đã copy repository URL
- [ ] Đã tạo Personal Access Token
- [ ] Đã kiểm tra .gitignore (loại bỏ files không cần)
- [ ] Đã test build project (./gradlew build)
- [ ] Đã test chạy application
- [ ] README.md đã đầy đủ thông tin
- [ ] Không có sensitive data (passwords, keys)

---

## 🚀 Commands Hoàn Chỉnh

### Lần Đầu Push

```bash
# Vào thư mục project
cd /home/p/deposit-banking

# Kiểm tra git status
git status

# Nếu chưa init
git init

# Add remote (thay YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/deposit-banking.git

# Add tất cả files
git add .

# Commit
git commit -m "Initial commit: Banking Deposit API with Spring Boot 3, Docker, Clean Architecture"

# Set branch
git branch -M main

# Push
git push -u origin main
```

### Lần Sau (Chỉnh Sửa)

```bash
# 1. Xem thay đổi
git status

# 2. Add files đã sửa
git add .

# 3. Commit
git commit -m "Update: mô tả thay đổi"

# 4. Push
git push
```

---

## 🔍 Xem Repository Trên GitHub

### Sau Khi Push Thành Công:

1. **Vào:** `https://github.com/your-username/deposit-banking`
2. **Sẽ thấy:**
   - README.md hiển thị ngay dưới tên repo
   - Code tab: Tất cả source code
   - Files được tổ chức như local

### Mẹo GitHub:

- **README.md** hiển thị ở trang chủ repository
- Click vào file .md để xem với Markdown formatting
- Click vào code để xem syntax highlighting
- Có thể edit trực tiếp trên GitHub web

---

## 📍 Tóm Tắt Vị Trí

**GitHub Repository Structure:**
```
github.com/your-username/deposit-banking/
├── Root level: Tất cả documentation và config
├── src/: Source code Java
├── gradle/: Gradle wrapper
└── Root files: build.gradle, Dockerfile, etc.
```

**Quy Tắc:**
- ✅ Documentation ở root để dễ tìm
- ✅ Source code ở src/
- ✅ Config files ở root
- ✅ .gitignore loại bỏ build files, logs

---

## 💡 Tips

1. **README.md** là file quan trọng nhất - hiển thị ngay khi vào repo
2. **Tổ chức gọn gàng** - dễ tìm và navigate
3. **Commit message rõ ràng** - giúp theo dõi lịch sử
4. **Branch main** là chuẩn mới (thay vì master)
5. **Private repo** cho project cá nhân/nội bộ

---

## 🎯 Kết Quả

Sau khi push thành công:

✅ Code trên GitHub: `github.com/your-username/deposit-banking`  
✅ Có thể clone về máy khác  
✅ Có thể share với team  
✅ Có thể tạo Issues và Pull Requests  
✅ Có lịch sử commit đầy đủ  

**Your project is now on GitHub! 🎉**

