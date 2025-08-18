# Multi-stage build for Service-S (Java 21, Spring Boot 3)
# 1) Build stage: use Maven with Temurin JDK 21
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy sources and build
COPY pom.xml .
COPY src ./src

# Package application without tests to speed up image build
RUN mvn -q -DskipTests package

# 2) Runtime stage: use slim JRE 21
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the fat jar from the previous stage
COPY --from=builder /app/target/*.jar /app/app.jar

# Default Spring Boot port for this service (can be overridden by SERVER_PORT env)
EXPOSE ${SERVICE_S_PORT}

# Allow passing JVM opts via env
ENV JAVA_OPTS=""

# Use exec form; sh -c to allow JAVA_OPTS expansion
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
