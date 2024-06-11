package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void LoadContext() throws Exception {
        Assertions
                .assertThat(this.facultyController).isNotNull();
    }

    @Test
    public void GetAllFaculties() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculties",
                        String.class)).isNotNull();
    }

    @Test
    public void GetStudent() throws Exception {
        Long id = facultyController.getAllFaculty().stream()
                .map(e -> e.getId()).min(Long::compareTo).get();

        Assertions.
                assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculties/" + id,
                        String.class)).isNotNull();
    }

    @Test
    public void getAllStudents() throws Exception {
        Long id = facultyController.getAllFaculty().stream()
                .map(e -> e.getId()).min(Long::compareTo).get();

        Assertions.
                assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculties/" + id + "/students",
                        String.class)).isNotNull();
    }

    @Test
    public void createFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("test");
        faculty.setColor("red");
        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/faculties",
                        faculty, String.class)).isNotNull();
    }

    @Test
    public void editFaculty() throws Exception {
        Faculty faculty = facultyController.getAllFaculty().stream()
                .filter(e -> e.getName().equals("test")).findFirst().orElse(new Faculty());

        faculty.setName("test2");
        faculty.setColor("yellow");
        faculty.setStudents(new ArrayList<>());

        this.restTemplate.put("http://localhost:" + port + "/faculties", faculty);
    }

    @Test
    public void deleteFaculty() throws Exception {
        Long id = facultyController.getAllFaculty().stream()
                .filter(e -> e.getName().equals("test2"))
                .map(e -> e.getId()).max(Long::compareTo).orElseGet(()->{
                    Faculty faculty = new Faculty();
                    facultyController.createFaculty(faculty);
                    return faculty.getId();
                });

        this.restTemplate.delete("http://localhost:" + port + "/faculties/" + id);
    }

    @Test
    public void findByNameOrColor() throws  Exception{
        Map<String,String> params= new HashMap<>();
        params.put("name","test");
        params.put("color","red");

        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:"+port+"/faculties",
                        String.class,params)).isNotNull();

    }
}