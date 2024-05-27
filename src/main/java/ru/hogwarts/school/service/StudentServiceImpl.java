package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final Map<Long, Student> students = new HashMap<>();
    private static Long lastId = 0L;

    @Override
    public Student getStudent(Long id) {
        return students.get(id);
    }

    @Override
    public Collection<Student> getAllStudents() {
        return Collections.unmodifiableCollection(students.values());
    }

    @Override
    public Student editStudent(Long id, Student student) {
        Student editedStudent = students.get(id);
        return students.put(editedStudent.getId(),
                new Student(editedStudent.getId(), student.getName(), student.getAge()));
    }

    @Override
    public Student createStudent(Student student) {
        Student createdStudent = new Student(++lastId, student.getName(), student.getAge());
        students.put(lastId, createdStudent);
        return createdStudent;
    }

    @Override
    public Student deleteStudent(Long id) {
        return students.remove(id);
    }

    @Override
    public Collection<Student> filterByAge(Integer age) {
        return Collections.unmodifiableCollection(
                students.values().stream().filter(e-> Objects.equals(e.getAge(),age)).toList());
    }
}
