# Multi-stage build for Service-R (Java 21, Spring Boot 3)
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Accept build arguments with defaults
ARG NEXUS_HOST=localhost

# Copy settings.xml template with placeholders
COPY settings.xml /root/.m2/settings.xml

# Copy project files
COPY pom.xml .
COPY src ./src

# Replace placeholders in settings.xml with build arguments
RUN sed -i "s|\${NEXUS_HOST}|$NEXUS_HOST|g" /root/.m2/settings.xml

# Build the project
RUN mvn -B -s /root/.m2/settings.xml clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/target/*.jar /app/app.jar

EXPOSE 8080
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
