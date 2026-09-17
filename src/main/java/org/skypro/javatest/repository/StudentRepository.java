package org.skypro.javatest.repository;

import org.skypro.javatest.obj.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAgeBetween(int one, int two);

    Collection<Student> findByFacultyStudentNameIgnoreCaseContaining(String nameFaculty);

    @Query(value = "SELECT COUNT(*) FROM STUDENT  ", nativeQuery = true)
    int findAllStudent();

    @Query(value = "SELECT AVG(age) FROM STUDENT", nativeQuery = true)
    int averageAge();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> findSomeStudent();

}
