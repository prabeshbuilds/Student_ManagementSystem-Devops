# Use Java 21 image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy jar file
COPY target/student-app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9090

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]