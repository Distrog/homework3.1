package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
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
import ru.hogwarts.school.service.StudentService;
import ru.hogwarts.school.service.StudentServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

        JSONArray studentObjects = new JSONArray();
        studentObjects.put(new JSONObject().put("id", 1L).put("name", "student1").put("age", 11));
        studentObjects.put(new JSONObject().put("id", 2L).put("name", "student2").put("age", 22));

        when(studentRepository.findAll()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students")
                        .content(studentObjects.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
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
                        .get("/students/1")
                        .content(studentJSON.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
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

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", 1L);
        facultyObject.put("name", "faculty1");
        facultyObject.put("color", "red");

        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/1/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
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
        JSONArray studentObjects = new JSONArray();
        studentObjects.put(new JSONObject().put("id", 1L).put("name", "student1").put("age", 11));

        when(studentRepository.findByAge(any(Integer.class))).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students")
                        .param("age", "11")
                        .content(studentObjects.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
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

        JSONArray studentObjects = new JSONArray();
        studentObjects.put(new JSONObject().put("id", 1L).put("name", "student1").put("age", 11));
        studentObjects.put(new JSONObject().put("id", 2L).put("name", "student2").put("age", 22));

        when(studentRepository.findByAgeBetween(any(Integer.class), any(Integer.class)))
                .thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students")
                        .param("minAge", "10")
                        .param("maxAge", "23")
                        .content(studentObjects.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(student1,student2))));
    }
}