package org.skypro.javatest.repository;


import org.skypro.javatest.obj.Faculty;
import org.skypro.javatest.obj.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Faculty findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(String name, String color);

    Collection<Student> findAllStudentsByFacultyStudentIgnoreCaseContains(String nameFaculty);

    Collection<Faculty> findAllFacultyByColorContainingIgnoreCase(String color);

}
