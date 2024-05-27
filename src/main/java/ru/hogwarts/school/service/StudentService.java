package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentService {
    Student getStudent(Long id);
    Collection<Student> getAllStudents();
    Student editStudent(Student student);
    Student createStudent(Student student);
    void deleteStudent(Long id);

    Collection<Student> filterByAge(Integer age);
}
