# Use Java 21 image
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy jar file
COPY target/student-app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]