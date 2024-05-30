package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("faculties")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(@RequestBody FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        Faculty foundedFaculty = facultyService.getFaculty(id);
        if (foundedFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundedFaculty);
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty editedFaculty = facultyService.editFaculty(faculty);
        if (editedFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(editedFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public Collection<Faculty> findByNameOrColor(@RequestParam(required = false) String name,
                                                 @RequestParam(required = false) String color) {
        if (name == null && color == null) {
            return facultyService.getAllFaculties();
        } else if (name == null) {
            return facultyService.findByColor(color);
        } else if (color == null) {
            return facultyService.findByName(name);
        } else {
            return facultyService.findByNameOrColor(name, color);
        }
    }
}
