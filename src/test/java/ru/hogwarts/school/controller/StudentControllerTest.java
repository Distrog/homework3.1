package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {
    @Mock
    private StudentService serviceMock;
    @InjectMocks
    private StudentController out;

    @Test
    public void shouldGetStudent() {
        when(serviceMock.getStudent(1)).thenReturn(new Student(1, "Name1", 1));
        assertEquals(ResponseEntity.ok(new Student(1, "Name1", 1)),
                out.getStudent(1L));
    }

    @Test
    public void shouldGetWrongStudent() {
        when(serviceMock.getStudent(1)).thenReturn(null);
        assertEquals(ResponseEntity.notFound().build(), out.getStudent(1L));
    }

    @Test
    public void shouldCreateStudent() {
        when(serviceMock.createStudent(new Student(0, "Name1", 1)))
                .thenReturn(new Student(0, "Name1", 1));
        assertEquals(out.createStudent(new Student(0, "Name1", 1)),
                ResponseEntity.ok(new Student(0, "Name1", 1)));
    }

    @Test
    public void shouldCreateWrongStudent() {
        when(serviceMock.createStudent(new Student(0, null, 0)))
                .thenReturn(null);
        assertEquals(out.createStudent(new Student(0, null, 0)),
                ResponseEntity.badRequest().build());
    }

    @Test
    public void shouldEditStudent() {
        when(serviceMock.editStudent(1L, new Student(0, "Name1", 1)))
                .thenReturn(new Student(1, "Name1", 1));
        assertEquals(out.editStudent(1L, new Student(0, "Name1", 1)),
                ResponseEntity.ok(new Student(1, "Name1", 1)));
    }

    @Test
    public void shouldEditNotFoundedStudent() {
        when(serviceMock.editStudent(1L, new Student(0, "Name1", 1)))
                .thenReturn(null);
        assertEquals(out.editStudent(1L, new Student(0, "Name1", 1)),
                ResponseEntity.notFound().build());
    }

    @Test
    public void shouldDeleteStudent() {
        when(serviceMock.getStudent(1L))
                .thenReturn(new Student(1, "Name1", 1));
        when(serviceMock.deleteStudent(1L))
                .thenReturn(new Student(1, "Name1", 1));
        assertEquals(out.deleteStudent(1L),
                ResponseEntity.ok(new Student(1, "Name1", 1)));
    }

    @Test
    public void shouldDeleteNotFoundedStudent() {
        when(serviceMock.getStudent(1L))
                .thenReturn(null);
        assertEquals(out.deleteStudent(1L),
                ResponseEntity.notFound().build());
    }

    @Test
    public void shouldGetAllStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "Name1", 1));
        students.add(new Student(2, "Name2", 2));
        students.add(new Student(3, "Name3", 3));
        when(serviceMock.getAllStudents())
                .thenReturn(students);

        assertEquals(out.getAllStudents(), ResponseEntity.ok(students));
    }

    @Test
    public void shouldFilterStudentsByAge() {
        Map<Long, Student> students = new HashMap<>();
        students.put(1L, new Student(1, "Name1", 1));
        students.put(2L, new Student(2, "Name2", 1));
        students.put(3L, new Student(3, "Name3", 1));

        when(serviceMock.filterByAge(1))
                .thenReturn(students.values());

        assertEquals(out.filterStudentsByAge(1),
                ResponseEntity.ok(students.values()));
    }
}