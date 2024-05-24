package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

public interface FacultyService {
    Faculty getFaculty(long id);

    Collection<Faculty> getAllFaculties();

    Faculty editFaculty(Long id, Faculty faculty);

    Faculty createFaculty(Faculty faculty);

    Faculty deleteFaculty(long id);

    Collection<Faculty> filterByColor(String color);
}
