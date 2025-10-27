FROM gradle:8.12-jdk17 AS build

WORKDIR /app

# Copy all project files
COPY . .

# Build the application using Gradle
RUN gradle clean build -x test --no-daemon

FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/build/libs/*.jar /app/deposit-banking.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/deposit-banking.jar"]

