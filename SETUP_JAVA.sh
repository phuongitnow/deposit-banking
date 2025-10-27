#!/bin/bash
# Quick setup for Java environment

# Check if Java is already installed
if command -v java &> /dev/null; then
    echo "Java found at: $(which java)"
    echo "Java version:"
    java -version 2>&1 | head -1
    exit 0
fi

echo "Java not found. Please run one of these commands:"
echo ""
echo "For Ubuntu/Debian:"
echo "  sudo apt-get update && sudo apt-get install -y openjdk-17-jdk"
echo ""
echo "Then set JAVA_HOME:"
echo "  export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64"
echo "  export PATH=\$JAVA_HOME/bin:\$PATH"
