# =============================================================================
# Stage 1: Build Stage
# =============================================================================
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

# Copy Maven wrapper and POM first for layer caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Convert Windows CRLF line endings to Linux LF and set execution permissions
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Download project dependencies (cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline -B

# Copy project source code
COPY src ./src

# Build production jar
RUN ./mvnw clean package -DskipTests

# =============================================================================
# Stage 2: Lightweight Production Runtime Stage
# =============================================================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Run as non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Create directory for file uploads and adjust permissions
RUN mkdir -p /app/uploads && chown -R appuser:appgroup /app

# Copy executable jar from builder stage
COPY --from=builder --chown=appuser:appgroup /build/target/*.jar app.jar

# Switch to non-root user
USER appuser

# Expose server port (default: 8088)
EXPOSE 8088

# Default environment variables
ENV PORT=8088 \
    UPLOAD_DIR=/app/uploads

# Run Spring Boot application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
