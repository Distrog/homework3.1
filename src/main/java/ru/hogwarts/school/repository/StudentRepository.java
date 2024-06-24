package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.List;


public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int from, int to);

    @Query(value = "select count(*) from student", nativeQuery = true)
    Integer getCountOfStudents();

    @Query(value = "select avg(age) from student",nativeQuery = true)
    Integer getAverageAgeOfStudents();
}
