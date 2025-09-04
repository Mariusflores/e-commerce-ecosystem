# Use official Java 21 JDK image
FROM eclipse-temurin:21-jdk-jammy

# Set working directory inside container
WORKDIR /app

# Copy the JAR into the container
COPY target/product-service-*.jar ./product-service.jar

# Expose the port your service uses
EXPOSE 8081

# Run the Spring Boot JAR
ENTRYPOINT ["java", "-jar", "product-service.jar"]
