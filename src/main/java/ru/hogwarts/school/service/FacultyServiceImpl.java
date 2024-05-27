package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final Map<Long, Faculty> faculties = new HashMap<>();
    private static Long lastId = 0L;

    @Override
    public Faculty getFaculty(Long id) {
        return faculties.get(id);
    }

    @Override
    public Collection<Faculty> getAllFaculties() {
        return Collections.unmodifiableCollection(faculties.values());
    }

    @Override
    public Faculty editFaculty(Long id, Faculty faculty) {
        Faculty editedFaculty = faculties.get(id);
        return faculties.put(editedFaculty.getId(),
                new Faculty(editedFaculty.getId(), faculty.getName(), faculty.getColor()));
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        Faculty createdFaculty = new Faculty(++lastId, faculty.getName(), faculty.getColor());
        faculties.put(lastId, createdFaculty);
        return createdFaculty;
    }

    @Override
    public Faculty deleteFaculty(Long id) {
        return faculties.remove(id);
    }

    @Override
    public Collection<Faculty> filterByColor(String color) {
        return Collections.unmodifiableCollection(
                faculties.values().stream().filter(e->e.getColor().equals(color)).toList());
    }
}
