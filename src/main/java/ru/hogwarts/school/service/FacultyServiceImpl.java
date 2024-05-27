package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository faculties;
@Autowired
    public FacultyServiceImpl(FacultyRepository faculties) {
        this.faculties = faculties;
    }

    @Override
    public Faculty getFaculty(Long id) {
        return faculties.findById(id).get();
    }

    @Override
    public Collection<Faculty> getAllFaculties() {
        return faculties.findAll();
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        return faculties.save(faculty);
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        return faculties.save(faculty);
    }

    @Override
    public void deleteFaculty(Long id) {
        faculties.deleteById(id);
    }

    @Override
    public Collection<Faculty> filterByColor(String color) {
        return faculties.findByColorLike(color);
    }
}
