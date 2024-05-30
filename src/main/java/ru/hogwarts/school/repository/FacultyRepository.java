package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty,Long> {
    List<Faculty> findByColorLike(String color);

    List<Faculty> findByNameIgnoreCaseAndColorIgnoreCase(String name, String color);

    List<Faculty> findByColorIgnoreCase(String color);

    List<Faculty> findByNameIgnoreCase(String name);
}
