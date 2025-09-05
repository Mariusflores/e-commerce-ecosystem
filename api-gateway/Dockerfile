# API gateway Dockerfile

# Use official Java 21 JDK image
FROM eclipse-temurin:21-jdk-jammy

# Set working directory inside container
WORKDIR /app

# Copy the JAR into the container
COPY target/api-gateway-*.jar ./api-gateway.jar

# Expose the port your service uses
EXPOSE 8080

# Run the Spring Boot JAR
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]