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
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(FacultyController.class)
class FacultyControllerMvcTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    FacultyRepository facultyRepository;

    @SpyBean
    FacultyServiceImpl facultyService;

    @InjectMocks
    FacultyController facultyController;

    @Test
    public void getAllFaculties() throws Exception {
        List<Faculty> faculties = new ArrayList<>();

        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setName("faculty1");
        faculty1.setColor("red");

        Faculty faculty2 = new Faculty();
        faculty2.setId(2L);
        faculty2.setName("faculty2");
        faculty2.setColor("yellow");
        faculties.add(faculty1);
        faculties.add(faculty2);

        JSONArray facultyObject = new JSONArray();
        facultyObject.put(new JSONObject().put("id", 1L).put("name", "faculty1").put("color", "red"));
        facultyObject.put(new JSONObject().put("id", 2L).put("name", "faculty2").put("color", "yellow"));

        when(facultyRepository.findAll()).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculties")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(faculty1,faculty2))));
    }

    @Test
    public void getFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("faculty1");
        faculty.setColor("red");

        JSONObject studentJSON = new JSONObject();
        studentJSON.put("id", 1L).put("name", "faculty1").put("color", "red");

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculties/1")
                        .content(studentJSON.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("faculty1"))
                .andExpect(jsonPath("color").value("red"));
    }

    @Test
    public void getStudents() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("faculty1");
        faculty.setColor("red");

        Student student = new Student();
        student.setId(1L);
        student.setName("student1");
        student.setAge(11);
        student.setFaculty(faculty);

        faculty.setStudents(new ArrayList<>(List.of(student)));
        JSONArray studentsObject = new JSONArray();

        studentsObject.put(new JSONObject().put("id", 1L).put("name", "faculty1").put("color", "red").put("faculty_id",1L));

        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculties/1/students")
                        .content(studentsObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("faculty1");
        faculty.setColor("red");

        JSONObject facultyJSON = new JSONObject();
        facultyJSON.put("id", 1L).put("name", "faculty1").put("color", "red");

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculties")
                        .content(facultyJSON.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("faculty1"))
                .andExpect(jsonPath("color").value("red"));
    }

    @Test
    public void editFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("faculty1");
        faculty.setColor("red");

        JSONObject facultyJSON = new JSONObject();
        facultyJSON.put("id", 1L).put("name", "faculty1").put("color", "red");

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculties")
                        .content(facultyJSON.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("faculty1"))
                .andExpect(jsonPath("color").value("red"));
    }

    @Test
    public void deleteFaculty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/faculties/1")).andExpect(status().isOk());
    }

    @Test
    public void findByColor() throws Exception {
        List<Faculty> faculties = new ArrayList<>();

        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setName("faculty1");
        faculty1.setColor("red");

        Faculty faculty2 = new Faculty();
        faculty2.setId(2L);
        faculty2.setName("faculty1");
        faculty2.setColor("red");

        faculties.add(faculty1);
        faculties.add(faculty2);

        JSONArray facultyObjects = new JSONArray();
        facultyObjects.put(new JSONObject().put("id", 1L).put("name", "faculty1").put("color", "red"));
        facultyObjects.put(new JSONObject().put("id", 2L).put("name", "faculty1").put("color", "red"));

        when(facultyRepository.findByNameIgnoreCaseAndColorIgnoreCase(any(String.class), any(String.class)))
                .thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculties")
                        .param("color", "red")
                        .param("name", "faculty1")
                        .content(facultyObjects.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("faculty1"))
                .andExpect(jsonPath("$[0].color").value("red"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("faculty1"))
                .andExpect(jsonPath("$[1].color").value("red"));
    }
}