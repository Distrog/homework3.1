package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

public interface FacultyService {
    Faculty getFaculty(Long id);

    Collection<Faculty> getAllFaculties();

    Faculty editFaculty(Long id, Faculty faculty);

    Faculty createFaculty(Faculty faculty);

    Faculty deleteFaculty(Long id);

    Collection<Faculty> filterByColor(String color);
}
