package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoad() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void getAllStudent() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students",
                        String.class))
                .isNotNull();
        ;
    }

    @Test
    public void getStudent() throws Exception {
        Long id = studentController.getAllStudents().stream()
                        .map(e->e.getId()).min(Long::compareTo).get();
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students/"+id,
                        String.class))
                .isNotNull();
    }

    @Test
    public void getFaculty() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students/1/faculty"
                        , String.class))
                .isNotNull();
    }

    @Test
    public void createStudent() throws Exception {
        Student student = new Student();
        student.setId(10L);
        student.setName("Test");
        student.setAge(44);
        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/students",
                        student, String.class))
                .isNotNull();
    }

    @Test
    public void editStudent() throws Exception {

        Student student = studentController.getAllStudents().stream()
                .filter(e -> e.getName().equals("Test"))
                .findFirst().orElse(new Student());
        student.setName("Test2");
        student.setAge(55);

        this.restTemplate.put("http://localhost:" + port + "/students", student);
    }

    @Test
    public void deleteStudent() throws Exception {
        Long id = studentController.getAllStudents().stream()
                .filter(e -> e.getName().equals("Test2"))
                .map(e -> e.getId()).max(Long::compareTo).get();

        this.restTemplate.delete("http://localhost:" + port + "/students/" + id);
    }

    @Test
    public void filterStudentsByAge() throws Exception{
        Map<String,Integer> params= new HashMap<>();
        params.put("age",44);
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:"+port+"/students",
                        String.class,params)).isNotNull();
    }

    @Test
    public void findByAgeBetween() throws Exception{
        Map<String,Integer> params= new HashMap<>();
        params.put("minAge",44);
        params.put("maxAge",55);
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:"+port+"/students",
                        String.class,params)).isNotNull();
    }
}