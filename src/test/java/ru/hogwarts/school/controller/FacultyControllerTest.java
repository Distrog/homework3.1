package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.util.UriComponentsBuilder;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    TestRestTemplate restTemplate;

    @BeforeEach
    public void clearAll() {
        facultyRepository.deleteAll();
    }

    @Test
    public void LoadContext() throws Exception {
        Assertions
                .assertThat(this.facultyRepository).isNotNull();
    }

    @Test
    public void GetAllFaculties() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setName("faculty1");
        faculty1.setColor("red");

        Faculty faculty2 = new Faculty();
        faculty1.setName("faculty2");
        faculty1.setColor("green");

        facultyRepository.save(faculty1);
        facultyRepository.save(faculty2);

        Faculty[] actual = this.restTemplate.getForObject("http://localhost:" + port + "/faculties",
                Faculty[].class);

        Assertions.assertThat(actual[0].getName()).isEqualTo(faculty1.getName());
        Assertions.assertThat(actual[0].getColor()).isEqualTo(faculty1.getColor());

        Assertions.assertThat(actual[1].getName()).isEqualTo(faculty2.getName());
        Assertions.assertThat(actual[1].getColor()).isEqualTo(faculty2.getColor());
    }

    @Test
    public void GetFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("faculty1");
        faculty.setColor("red");

        Faculty actual = this.restTemplate.getForObject("http://localhost:" + port + "/faculties/" + faculty.getId(),
                Faculty.class);

        Assertions.assertThat(actual).isEqualTo(faculty);
    }

    @Test
    public void getAllStudents() throws Exception {
        List<Student> students = new ArrayList<>();

        Faculty faculty = new Faculty();
        faculty.setName("faculty");
        faculty.setColor("red");
        facultyRepository.save(faculty);

        Student student1 = new Student();
        student1.setName("student1");
        student1.setAge(11);
        student1.setFaculty(faculty);
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setName("student2");
        student2.setAge(22);
        student2.setFaculty(faculty);
        studentRepository.save(student2);

        students.add(student1);
        students.add(student2);

        Student[] actual = this.restTemplate.getForObject("http://localhost:" + port + "/faculties/"
                + faculty.getId() + "/students", Student[].class);

        Assertions.assertThat(actual.length).isEqualTo(2);
        Assertions.assertThat(actual[0]).isEqualTo(student1);
        Assertions.assertThat(actual[1]).isEqualTo(student2);
    }

    @Test
    public void createFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("test");
        faculty.setColor("red");

        Faculty actual = this.restTemplate.postForObject("http://localhost:" + port + "/faculties",
                faculty, Faculty.class);

        Assertions.assertThat(actual.getName()).isEqualTo(faculty.getName());
        Assertions.assertThat(actual.getColor()).isEqualTo(faculty.getColor());
    }

    @Test
    public void editFaculty() throws Exception {
        Faculty createdFaculty = new Faculty();
        createdFaculty.setName("test");
        createdFaculty.setColor("red");
        facultyRepository.save(createdFaculty);

        Faculty editedFaculty = new Faculty();
        editedFaculty.setId(createdFaculty.getId());
        editedFaculty.setName("test2");
        editedFaculty.setColor("yellow");

        this.restTemplate.put("http://localhost:" + port + "/faculties", editedFaculty);

        Assertions.assertThat(facultyRepository.findById(editedFaculty.getId()).get().getName())
                .isEqualTo("test2");

        Assertions.assertThat(facultyRepository.findById(editedFaculty.getId()).get().getColor())
                .isEqualTo("yellow");
    }

    @Test
    public void deleteFaculty() throws Exception {
        Faculty createdFaculty = new Faculty();
        createdFaculty.setName("test");
        createdFaculty.setColor("red");
        facultyRepository.save(createdFaculty);

        this.restTemplate.delete("http://localhost:" + port + "/faculties/" + createdFaculty.getId());

        Assertions.assertThat(facultyRepository.findById(createdFaculty.getId())).isEmpty();
    }

    @Test
    public void findByNameOrColor() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setName("test1");
        faculty1.setColor("red");
        facultyRepository.save(faculty1);

        Faculty faculty2 = new Faculty();
        faculty1.setName("test2");
        faculty1.setColor("green");
        facultyRepository.save(faculty2);

        Faculty faculty3 = new Faculty();
        faculty1.setName("test3");
        faculty1.setColor("yellow");
        facultyRepository.save(faculty3);

        String uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port
                        + "/faculties").queryParam("name", "test1")
                .queryParam("color", "red").toUriString();

        Faculty[] actual = this.restTemplate.getForObject(uri, Faculty[].class);

        Assertions.assertThat(actual.length).isEqualTo(1);
        Assertions.assertThat(actual[0].getName()).isEqualTo("test1");
        Assertions.assertThat(actual[0].getColor()).isEqualTo("red");
    }
}