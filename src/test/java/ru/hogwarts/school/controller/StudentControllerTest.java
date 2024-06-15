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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoad() throws Exception {
        Assertions.assertThat(studentRepository).isNotNull();
    }

    @BeforeEach
    public void clearAll() {
       studentRepository.deleteAll();
    }

    @Test
    public void getAllStudent() throws Exception {
        Student student1 = new Student();
        student1.setName("student1");
        student1.setAge(11);

        Student student2 = new Student();
        student2.setName("student2");
        student2.setAge(22);

        studentRepository.save(student1);
        studentRepository.save(student2);

        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students",
                        Student[].class))
                .isNotNull()
                .contains(student1)
        ;
    }

    @Test
    public void getStudent() throws Exception {

        Student student = new Student();
        student.setName("student");
        student.setAge(11);

        studentRepository.save(student);
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students/1",
                        String.class))
                .isNotNull()
                .isEqualTo("{\"id\":1,\"name\":\"student\",\"age\":11}");
    }

    @Test
    public void getFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("faculty");
        faculty.setColor("red");

        facultyRepository.save(faculty);

        Student student = new Student();
        student.setName("student");
        student.setAge(11);
        student.setFaculty(faculty);


        studentRepository.save(student);
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students/"+student.getId()+"/faculty"
                        , String.class))
                .isNotNull()
                .contains("\"name\":\"faculty\"")
                .contains("\"color\":\"red\"");
    }

    @Test
    public void createStudent() throws Exception {
        Student expected = new Student();
        expected.setName("Test");
        expected.setAge(22);

        Student actual = this.restTemplate.postForObject("http://localhost:" + port + "/students",
                expected, Student.class);

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getName()).isEqualTo(expected.getName());
        Assertions.assertThat(actual.getAge()).isEqualTo(expected.getAge());
    }

    @Test
    public void editStudent() throws Exception {
        Student newStudent = new Student();
        newStudent.setName("Test");
        newStudent.setAge(22);

        Student createdStudent = this.restTemplate.postForObject("http://localhost:" + port + "/students",
                newStudent, Student.class);

        Student expected = new Student();
        expected.setId(createdStudent.getId());
        expected.setName("Test2");
        expected.setAge(33);

        this.restTemplate.put("http://localhost:" + port + "/students", expected);

        Assertions.assertThat(this.studentRepository.findById(expected.getId()).get()).isEqualTo(expected);
    }

    @Test
    public void deleteStudent() throws Exception {
        Student createdStudent = new Student();
        createdStudent.setId(1L);
        createdStudent.setName("Test");
        createdStudent.setAge(22);

        this.restTemplate.delete("http://localhost:" + port + "/students/" + createdStudent.getId());
       Assertions.assertThat(this.studentRepository.findById(1L)).isEmpty();
    }

    @Test
    public void filterStudentsByAge() throws Exception{
        Student createdStudent1 = new Student();
        createdStudent1.setId(1L);
        createdStudent1.setName("Test");
        createdStudent1.setAge(44);
        this.restTemplate.postForEntity("http://localhost:" + port + "/students",createdStudent1,
                Student.class);

        Student createdStudent2 = new Student();
        createdStudent2.setName("Test2");
        createdStudent2.setAge(55);
        this.restTemplate.postForEntity("http://localhost:" + port + "/students",createdStudent2,
                Student.class);

        Student createdStudent3 = new Student();
        createdStudent3.setName("Test3");
        createdStudent3.setAge(66);
        this.restTemplate.postForEntity("http://localhost:" + port + "/students",createdStudent3,
                Student.class);

        String uri = UriComponentsBuilder.fromHttpUrl("http://localhost:"+port+"/students")
                .queryParam("age",44).toUriString();
        Student[] actual = this.restTemplate.getForObject(uri, Student[].class);

        Assertions.assertThat(actual.length).isEqualTo(1);
        Assertions.assertThat(actual[0].getName()).isEqualTo("Test");
        Assertions.assertThat(actual[0].getAge()).isEqualTo(44);
    }

    @Test
    public void findByAgeBetween() throws Exception{
        Student createdStudent1 = new Student();
        createdStudent1.setId(1L);
        createdStudent1.setName("Test");
        createdStudent1.setAge(44);
        this.restTemplate.postForEntity("http://localhost:" + port + "/students",createdStudent1,
                Student.class);

        Student createdStudent2 = new Student();
        createdStudent2.setId(2);
        createdStudent2.setName("Test2");
        createdStudent2.setAge(55);
        this.restTemplate.postForEntity("http://localhost:" + port + "/students",createdStudent2,
                Student.class);

        Student createdStudent3 = new Student();
        createdStudent3.setName("Test3");
        createdStudent3.setAge(66);
        this.restTemplate.postForEntity("http://localhost:" + port + "/students",createdStudent3,
                Student.class);

        String uri = UriComponentsBuilder.fromHttpUrl("http://localhost:"+port+"/students?minAge=44&maxAge=55")
                .queryParam("minAge",44).queryParam("maxAge",55).toUriString();

        Student[] actual = this.restTemplate.getForObject(uri, Student[].class);

        Assertions.assertThat(actual.length).isEqualTo(2);
        Assertions.assertThat(actual[0].getAge()).isEqualTo(44);
        Assertions.assertThat(actual[0].getName()).isEqualTo("Test");
        Assertions.assertThat(actual[1].getAge()).isEqualTo(55);
        Assertions.assertThat(actual[1].getName()).isEqualTo("Test2");
    }
}