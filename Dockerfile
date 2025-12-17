# ==============================
# Multi-stage Dockerfile for Kuspid Artist Management
# ==============================

# ------------------------------
# Stage 1: Build
# ------------------------------
FROM maven:3.9.5-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Copy pom.xml first (better caching)
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source
COPY src ./src

# Build JAR (skip tests for faster CI)
RUN mvn clean package -DskipTests -B

# ------------------------------
# Stage 2: Runtime
# ------------------------------
FROM eclipse-temurin:17-jre-alpine

# Install minimal required packages
RUN apk add --no-cache curl bash

# Create non-root user (security best practice)
RUN addgroup -S appuser && adduser -S appuser -G appuser

WORKDIR /app

# Copy application JAR
COPY --from=builder /app/target/*.jar app.jar

# Create runtime directories
RUN mkdir -p /app/storage /app/logs && \
    chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port (Render maps this automatically)
EXPOSE 8080

# JVM options tuned for Render Free tier
ENV JAVA_OPTS="-Xms256m \
    -Xmx512m \
    -XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:+ExitOnOutOfMemoryError \
    -Djava.security.egd=file:/dev/./urandom"

# Health check (matches render.yaml)
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
