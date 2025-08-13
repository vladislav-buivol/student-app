# Multi-stage build for Service-R (Java 21, Spring Boot 3)

FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

COPY settings.xml /root/.m2/settings.xml
COPY pom.xml .
COPY src ./src

RUN mvn -B -s /root/.m2/settings.xml clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar

EXPOSE 8080

ENV JAVA_OPTS=""


ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
