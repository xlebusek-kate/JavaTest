package org.skypro.javatest.repository;

import org.skypro.javatest.obj.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAgeBetween(int one, int two);

    Collection<Student> findAllStudentsByFacultyStudentIgnoreCaseContains(String nameFaculty);

    Student findStudentById(long id);

}
