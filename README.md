# Student Management System (DevOps Demo)

A simple Spring Boot student management application with CRUD APIs, JPA persistence, and PostgreSQL support.

## Project overview

- Java 21
- Spring Boot 3.3.5
- Spring Web, Spring Data JPA, Thymeleaf, Actuator
- PostgreSQL runtime driver
- H2 runtime dependency available for quick local testing
- REST endpoints under `/students`

## Features

- Create, read, update, and delete student records
- Retrieve all students or a student by ID
- Health and version checks
- Spring Data JPA repository storage
- Configured for local and production profiles

## Requirements

- Java 21 or newer
- Maven 3.8+ or the included Maven wrapper
- PostgreSQL database (default: `student_db`)

## Default configuration

The local configuration is defined in `src/main/resources/application.properties`:

- `server.port=8080`
- `spring.datasource.url=jdbc:postgresql://localhost:5432/student_db`
- `spring.datasource.username=postgres`
- `spring.datasource.password=mypassword123`
- `spring.jpa.hibernate.ddl-auto=update`

For production, use the `prod` profile and environment variables:

- `SPRING_PROFILES_ACTIVE=prod`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SERVER_PORT`

## Build and run

Using the Maven wrapper:

```bash
./mvnw clean package
java -jar target/student-app-0.0.1-SNAPSHOT.jar
```

Or using Maven directly:

```bash
mvn clean package
java -jar target/student-app-0.0.1-SNAPSHOT.jar
```

### Run with prod profile

```bash
SPRING_PROFILES_ACTIVE=prod \
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/student_db \
SPRING_DATASOURCE_USERNAME=postgres \
SPRING_DATASOURCE_PASSWORD=mypassword123 \
SERVER_PORT=9090 \
java -jar target/student-app-0.0.1-SNAPSHOT.jar
```

## API endpoints

Base URL: `http://localhost:8080`

### Create student

```http
POST /students
Content-Type: application/json

{
  "name": "Jane Doe",
  "email": "jane.doe@example.com"
}
```

### Get all students

```http
GET /students
```

### Get student by ID

```http
GET /students/{id}
```

### Update student

```http
PUT /students/{id}
Content-Type: application/json

{
  "name": "Jane Doe Updated",
  "email": "jane.updated@example.com"
}
```

### Delete student

```http
DELETE /students/{id}
```

### Health check

```http
GET /students/health
```

### Version check

```http
GET /students/version
```

## Example curl commands

```bash
curl -X POST http://localhost:8080/students \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice","email":"alice@example.com"}'

curl http://localhost:8080/students

curl http://localhost:8080/students/1
```

## Notes

- The application uses JPA entity `Student` with `id`, `name`, and `email`.
- The repository is implemented by `StudentRepository`.
- The REST controller is `StudentController`.
- Local database changes are persisted to PostgreSQL by default.
- Use H2 only for temporary testing if you switch the datasource settings.

## Project files

- `src/main/java/com/example/studentapp/StudentAppApplication.java`
- `src/main/java/com/example/studentapp/controller/StudentController.java`
- `src/main/java/com/example/studentapp/model/Student.java`
- `src/main/java/com/example/studentapp/repository/StudentRepository.java`
- `src/main/resources/application.properties`
- `src/main/resources/application-prod.properties`

## License

This repository does not include a license file. Add one if you plan to share or publish the project.
