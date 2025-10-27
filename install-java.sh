#!/bin/bash
# Java Installation Script for Ubuntu/Debian

echo "Installing OpenJDK 17..."

sudo apt-get update
sudo apt-get install -y openjdk-17-jdk

# Set JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

# Add to .bashrc
if ! grep -q "JAVA_HOME" ~/.bashrc; then
    echo "" >> ~/.bashrc
    echo "# Java Environment" >> ~/.bashrc
    echo "export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" >> ~/.bashrc
    echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> ~/.bashrc
    echo "Java environment variables added to ~/.bashrc"
fi

echo "Installation complete!"
echo "Please run: source ~/.bashrc"
echo "Then verify with: java -version"

