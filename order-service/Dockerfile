# Order service Dockerfile

# Use official Java 21 JDK image
FROM eclipse-temurin:21-jdk-jammy

# Set working directory inside container
WORKDIR /app

# Copy the JAR into the container
COPY target/order-service-*.jar ./order-service.jar

# Expose the port your service uses
EXPOSE 8083

# Run the Spring Boot JAR
ENTRYPOINT ["java", "-jar", "order-service.jar"]