package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;


@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository students;

    @Autowired
    public StudentServiceImpl(StudentRepository students) {
        this.students = students;
    }

    @Override
    public Student getStudent(Long id) {
        return students.findById(id).get();
    }

    @Override
    public Collection<Student> getAllStudents() {
        return students.findAll();
    }

    @Override
    public Student editStudent(Student student) {
        return students.save(student);
    }

    @Override
    public Student createStudent(Student student) {
        return students.save(student);
    }

    @Override
    public void deleteStudent(Long id) {
        students.deleteById(id);
    }

    @Override
    public Collection<Student> filterByAge(Integer age) {
        return students.findByAge(age);
    }
}
