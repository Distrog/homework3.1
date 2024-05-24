package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentService {
    Student getStudent(long id);
    Collection<Student> getAllStudents();
    Student editStudent(Long id,Student student);
    Student createStudent(Student student);
    Student deleteStudent(long id);

    Collection<Student> filterByAge(Integer age);
}
