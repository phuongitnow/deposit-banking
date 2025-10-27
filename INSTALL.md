# Installation Guide

## Quick Install Java 17

### For Ubuntu/Debian:

```bash
# Install Java 17
sudo apt-get update
sudo apt-get install -y openjdk-17-jdk

# Set JAVA_HOME (temporary for current session)
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Set JAVA_HOME permanently
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc

# Reload shell
source ~/.bashrc

# Verify
java -version
```

### Using the provided script:

```bash
chmod +x install-java.sh
./install-java.sh
source ~/.bashrc
```

---

## Alternative: Use Docker Only

If you just want to run the application without installing Java locally:

```bash
# Start PostgreSQL
docker run -d --name banking-postgres \
  -e POSTGRES_DB=banking_db \
  -e POSTGRES_USER=banking_user \
  -e POSTGRES_PASSWORD=banking_pass \
  -p 5432:5432 \
  postgres:16-alpine

# Start the Spring Boot app (built inside container)
docker compose build
docker compose up
```

Note: The Docker build will download Gradle and compile the application inside the container.

---

## After Installing Java

Build and run the project:

```bash
# Build
./gradlew clean build

# Run tests
./gradlew test

# Run the application
./gradlew bootRun
```

The application will start on http://localhost:8080

---

## Troubleshooting

### JAVA_HOME not found
```bash
# Find where Java is installed
ls -la /usr/lib/jvm/

# Set JAVA_HOME (replace with actual path)
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
```

### Verify installation
```bash
java -version
echo $JAVA_HOME
which java
```

