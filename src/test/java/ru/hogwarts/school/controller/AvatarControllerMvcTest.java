package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AvatarController.class)
class AvatarControllerMvcTest {
    @Autowired
    MockMvc mockMvc;

    //    @MockBean
//    AvatarRepository avatarRepository;
    @MockBean
    StudentRepository studentRepository;
    @MockBean
    AvatarServiceImpl avatarService;

    @InjectMocks
    AvatarController avatarController;

    @Test
    public void GetAllAvatars() throws Exception {
        List<Avatar> avatars = new ArrayList<>();

        Avatar avatar1 = new Avatar();
        avatar1.setId(1L);

        Avatar avatar2 = new Avatar();
        avatar2.setId(2L);

        Avatar avatar3 = new Avatar();
        avatar3.setId(3L);

        avatars.add(avatar1);
        avatars.add(avatar2);
        avatars.add(avatar3);

        when(avatarService.getAllAvatars(1, 3)).thenReturn(avatars);

        mockMvc.perform(MockMvcRequestBuilders.get("/students/allAvatars")
                        .param("pageNumber", "1")
                        .param("pageSize", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].url")
                        .value("http://localhost:8080/students/1/avatar-from-db"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].url")
                        .value("http://localhost:8080/students/2/avatar-from-db"))
                .andExpect(jsonPath("$[2]id").value(3L))
                .andExpect(jsonPath("$[2].url")
                        .value("http://localhost:8080/students/3/avatar-from-db"));
    }
}