package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

public interface FacultyService {
    Faculty getFaculty(Long id);

    Collection<Faculty> getAllFaculties();

    Faculty editFaculty(Faculty faculty);

    Faculty createFaculty(Faculty faculty);

    void deleteFaculty(Long id);

    Collection<Faculty> filterByColor(String color);
}
