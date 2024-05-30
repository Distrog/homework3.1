package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public Collection<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student foundedStudent = studentService.getStudent(id);
        if (foundedStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundedStudent);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student editedStudent = studentService.editStudent(student);
        if (editedStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(editedStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
    studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(params = "age")
    public Collection<Student> filterStudentsByAge(@RequestParam("age") Integer age) {
        return studentService.filterByAge(age);
    }

    @GetMapping(params={"minAge", "maxAge"})
    public Collection<Student> findByAgeBetween(@RequestParam Integer minAge,
                                                @RequestParam Integer maxAge){
        return studentService.findByAgeBetween(minAge,maxAge);
    }
}
