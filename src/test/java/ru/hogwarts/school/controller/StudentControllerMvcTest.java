package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentServiceImpl;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private StudentRepository studentRepository;

    @SpyBean
    private StudentServiceImpl studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    public void getAllStudents() throws Exception {
        List<Student> students = new ArrayList<>();

        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("student1");
        student1.setAge(11);

        Student student2 = new Student();
        student1.setId(2L);
        student1.setName("student2");
        student1.setAge(22);
        students.add(student1);
        students.add(student2);

        when(studentRepository.findAll()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(student1,student2))));
    }

    @Test
    public void getStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("student1");
        student.setAge(11);

        JSONObject studentJSON = new JSONObject();
        studentJSON.put("id", 1L).put("name", "student1").put("age", 11);

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("student1"))
                .andExpect(jsonPath("age").value(11));
    }

    @Test
    public void getFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("faculty1");
        faculty.setColor("red");

        Student student = new Student();
        student.setId(1L);
        student.setName("student1");
        student.setAge(11);
        student.setFaculty(faculty);

        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("faculty1"))
                .andExpect(jsonPath("color").value("red"));
    }

    @Test
    public void createStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("student1");
        student.setAge(11);

        JSONObject studentJSON = new JSONObject();
        studentJSON.put("id", 1L).put("name", "student1").put("age", 11);

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/students")
                        .content(studentJSON.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("student1"))
                .andExpect(jsonPath("age").value(11));
    }

    @Test
    public void editStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("student1");
        student.setAge(11);

        JSONObject studentJSON = new JSONObject();
        studentJSON.put("id", 1L).put("name", "student1").put("age", 11);

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/students")
                        .content(studentJSON.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("student1"))
                .andExpect(jsonPath("age").value(11));
    }

    @Test
    public void deleteStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/students/1")).andExpect(status().isOk());
    }

    @Test
    public void filterStudentByAge() throws Exception {
        List<Student> students = new ArrayList<>();

        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("student1");
        student1.setAge(11);

        students.add(student1);

        when(studentRepository.findByAge(any(Integer.class))).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students")
                        .param("age", "11"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(student1))));
    }

    @Test
    public void findByAgeBetween() throws Exception {
        List<Student> students = new ArrayList<>();

        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("student1");
        student1.setAge(11);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("student2");
        student2.setAge(22);
        students.add(student1);
        students.add(student2);

        when(studentRepository.findByAgeBetween(any(Integer.class), any(Integer.class)))
                .thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students")
                        .param("minAge", "10")
                        .param("maxAge", "23"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(student1,student2))));
    }
    @Test
    public void getCountOfStudents() throws Exception{
        List<Student> students = new ArrayList<>();

        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("student1");
        student1.setAge(11);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("student2");
        student2.setAge(22);

        Student student3 = new Student();
        student2.setId(3L);
        student2.setName("student3");
        student2.setAge(33);

        students.add(student1);
        students.add(student2);
        students.add(student3);

        when(studentRepository.getCountOfStudents()).thenReturn(3);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(students.size())));
    }


    @Test
    public void getAverageAgeOfStudents() throws Exception{
        List<Student> students = new ArrayList<>();

        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("student1");
        student1.setAge(11);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("student2");
        student2.setAge(22);

        Student student3 = new Student();
        student2.setId(3L);
        student2.setName("student3");
        student2.setAge(33);

        students.add(student1);
        students.add(student2);
        students.add(student3);

        Integer averageAge = ((int) students.stream().mapToInt(student ->
                student.getAge()).average().getAsDouble());

        when(studentRepository.getAverageAgeOfStudents()).thenReturn(averageAge);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/avgAge"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(averageAge)));
    }

    @Test
    public void getFiveLastStudent() throws Exception{
        List<Student> students = new ArrayList<>();

        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("student1");
        student1.setAge(11);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("student2");
        student2.setAge(22);

        Student student3 = new Student();
        student2.setId(3L);
        student2.setName("student3");
        student2.setAge(33);

        Student student4 = new Student();
        student1.setId(4L);
        student1.setName("student4");
        student1.setAge(44);

        Student student5 = new Student();
        student2.setId(25L);
        student2.setName("student5");
        student2.setAge(55);

        Student student6 = new Student();
        student2.setId(6L);
        student2.setName("student6");
        student2.setAge(66);

        students.add(student6);
        students.add(student5);
        students.add(student4);
        students.add(student3);
        students.add(student2);

        when(studentRepository.getFiveLastStudents()).thenReturn(List.of(student6,student5,student4,student3,student2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/get-five-last-students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(students)));
    }
}