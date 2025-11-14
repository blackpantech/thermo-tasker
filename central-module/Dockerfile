# Build stage
FROM maven:3.9-eclipse-temurin-25-alpine AS builder
WORKDIR /app
COPY ./central-module/pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY ./central-module/src ./src
RUN mvn package

# Runtime stage
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar central-module.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "central-module.jar"]
