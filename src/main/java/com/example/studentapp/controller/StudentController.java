package com.example.studentapp.controller;

import com.example.studentapp.model.Student;
import com.example.studentapp.repository.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    // ➕ Create Student
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student saved = repository.save(student);
        return ResponseEntity.ok(saved);
    }

    // 📄 Get All Students
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {

        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Health Check</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f4f6f9;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        height: 100vh;
                        margin: 0;
                    }

                    .card {
                        background: white;
                        padding: 40px;
                        border-radius: 12px;
                        box-shadow: 0 4px 10px rgba(0,0,0,0.1);
                        text-align: center;
                    }

                    h1 {
                        color: #28a745;
                    }

                    p {
                        color: #555;
                        font-size: 18px;
                    }
                </style>
            </head>
            <body>
                <div class="card">
                    <h1>Hello Buddy Application is Running</h1>
                    <p>Your Spring Boot service is healthy.</p>
                </div>
            </body>
            </html>
            """;

        return ResponseEntity.ok()
                .header("Content-Type", "text/html")
                .body(html);
    }

    @GetMapping("/version")
    public ResponseEntity<String> versionCheck() {
        return ResponseEntity.ok("Application version: 1.0.0");
    }



    // 🔍 Get Student by ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✏️ Update Student
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable int id,
            @RequestBody Student updatedStudent) {

        return repository.findById(id)
                .map(student -> {
                    student.setName(updatedStudent.getName());
                    student.setEmail(updatedStudent.getEmail());
                    Student saved = repository.save(student);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ❌ Delete Student
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id) {

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.ok("Student deleted successfully");
    }
}