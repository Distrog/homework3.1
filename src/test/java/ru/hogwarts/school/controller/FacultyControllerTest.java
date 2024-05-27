package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FacultyControllerTest {
    @Mock
    private FacultyService serviceMock;
    @InjectMocks
    private FacultyController out;

    @Test
    public void shouldGetFaculty() {
        when(serviceMock.getFaculty(1L)).thenReturn(new Faculty(1, "Name1", "Color1"));
        assertEquals(ResponseEntity.ok(new Faculty(1, "Name1", "Color1")),
                out.getFaculty(1L));
    }

    @Test
    public void shouldGetWrongFaculty() {
        when(serviceMock.getFaculty(1L)).thenReturn(null);
        assertEquals(ResponseEntity.notFound().build(), out.getFaculty(1L));
    }

    @Test
    public void shouldCreateFaculty() {
        when(serviceMock.createFaculty(new Faculty(0, "Name1", "Color1")))
                .thenReturn(new Faculty(0, "Name1", "Color1"));
        assertEquals(out.createFaculty(new Faculty(0, "Name1", "Color1")),
                ResponseEntity.ok(new Faculty(0, "Name1", "Color1")));
    }

    @Test
    public void shouldCreateWrongFaculty() {
        when(serviceMock.createFaculty(new Faculty(0, null, null)))
                .thenReturn(null);
        assertEquals(out.createFaculty(new Faculty(0, null, null)),
                ResponseEntity.badRequest().build());
    }

    @Test
    public void shouldEditFaculty() {
        when(serviceMock.editFaculty(1L, new Faculty(0, "Name1", "Color1")))
                .thenReturn(new Faculty(1, "Name1", "Color1"));
        assertEquals(out.editFaculty(1L, new Faculty(0, "Name1", "Color1")),
                ResponseEntity.ok(new Faculty(1, "Name1", "Color1")));
    }

    @Test
    public void shouldEditNotFoundedFaculty() {
        when(serviceMock.editFaculty(1L, new Faculty(0, "Name1", "Color1")))
                .thenReturn(null);
        assertEquals(out.editFaculty(1L, new Faculty(0, "Name1", "Color1")),
                ResponseEntity.notFound().build());
    }

    @Test
    public void shouldDeleteFaculty() {
        when(serviceMock.getFaculty(1L))
                .thenReturn(new Faculty(1, "Name1", "Color1"));
        when(serviceMock.deleteFaculty(1L))
                .thenReturn(new Faculty(1, "Name1", "Color1"));
        assertEquals(out.deleteFaculty(1L),
                ResponseEntity.ok(new Faculty(1, "Name1", "Color1")));
    }

    @Test
    public void shouldDeleteNotFoundedFaculty() {
        when(serviceMock.getFaculty(1L))
                .thenReturn(null);
        assertEquals(out.deleteFaculty(1L),
                ResponseEntity.notFound().build());
    }

    @Test
    public void shouldGetAllFaculties() {
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(new Faculty(1, "Name1", "Colo1"));
        faculties.add(new Faculty(2, "Name2", "Colo2"));
        faculties.add(new Faculty(3, "Name3", "Colo3"));
        when(serviceMock.getAllFaculties())
                .thenReturn(faculties);

        assertEquals(out.getAllFaculties(), ResponseEntity.ok(faculties));
    }

    @Test
    public void shouldFilterFacultiesByColor() {
        Map<Long, Faculty> faculties = new HashMap<>();
        faculties.put(1L, new Faculty(1, "Name1", "Colo1"));
        faculties.put(2L, new Faculty(2, "Name2", "Colo1"));
        faculties.put(3L, new Faculty(3, "Name3", "Colo1"));

        when(serviceMock.filterByColor("Color1"))
                .thenReturn(faculties.values());

        assertEquals(out.filterFacultiesByColor("Color1"),
                ResponseEntity.ok(faculties.values()));
    }
}