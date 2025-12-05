# API gateway Dockerfile

# Stage 1: Build
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /workspace

# Copy parent pom
COPY pom.xml ./pom.xml

# Copy Maven wrapper
COPY .mvn ./.mvn
COPY mvnw ./mvnw

# Copy all module poms (Maven needs to see module structure)
COPY domain/pom.xml ./domain/pom.xml
COPY api-gateway/pom.xml ./api-gateway/pom.xml
COPY product-service/pom.xml ./product-service/pom.xml
COPY order-service/pom.xml ./order-service/pom.xml
COPY inventory-service/pom.xml ./inventory-service/pom.xml
COPY user-service/pom.xml ./user-service/pom.xml
COPY payments-service/pom.xml ./payments-service/pom.xml

# Copy domain source and build it
COPY domain/src ./domain/src
RUN ./mvnw clean install -pl domain -am -DskipTests

# Download dependencies for api-gateway (cached layer)
WORKDIR /workspace/api-gateway
RUN ../mvnw dependency:go-offline -B

# Copy source code
COPY api-gateway/src ./src

# Build the application
RUN ../mvnw clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the JAR from build stage
COPY --from=build /workspace/api-gateway/target/api-gateway-*.jar ./api-gateway.jar

# Expose the port
EXPOSE 8080

# Run the Spring Boot JAR
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]