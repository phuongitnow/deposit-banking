#!/bin/bash
# Script để push code lên GitHub

echo "🚀 Chào mừng! Đây là script để push code lên GitHub"
echo ""

# Kiểm tra git
if ! command -v git &> /dev/null; then
    echo "❌ Git chưa được cài đặt"
    echo "Cài đặt: sudo apt-get install git"
    exit 1
fi

echo "✅ Git đã được cài đặt"
echo ""

# Kiểm tra git init
if [ ! -d .git ]; then
    echo "📦 Đang khởi tạo git repository..."
    git init
fi

# Hỏi remote URL
read -p "📝 Nhập URL repository trên GitHub (ví dụ: https://github.com/username/repo.git): " REPO_URL

if [ -z "$REPO_URL" ]; then
    echo "❌ URL không được để trống"
    exit 1
fi

# Add remote
echo ""
echo "🔗 Đang thêm remote..."
git remote add origin $REPO_URL 2>/dev/null || git remote set-url origin $REPO_URL

# Add files
echo "📝 Đang add files..."
git add .

# Commit
echo ""
read -p "📝 Nhập message cho commit (hoặc nhấn Enter để dùng mặc định): " COMMIT_MSG
if [ -z "$COMMIT_MSG" ]; then
    COMMIT_MSG="Initial commit: Banking Deposit API with Spring Boot 3, Docker, PostgreSQL"
fi

git commit -m "$COMMIT_MSG"

# Push
echo ""
echo "⬆️  Đang push lên GitHub..."
git branch -M main
git push -u origin main

if [ $? -eq 0 ]; then
    echo ""
    echo "🎉 Push thành công!"
    echo "🔗 Xem repository: $REPO_URL"
else
    echo ""
    echo "❌ Push thất bại"
    echo "💡 Kiểm tra lại Personal Access Token hoặc SSH key"
    echo "📖 Xem hướng dẫn chi tiết trong: HUONG_DAN_GITHUB.md"
fi

