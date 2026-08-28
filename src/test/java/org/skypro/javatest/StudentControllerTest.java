package org.skypro.javatest;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skypro.javatest.obj.Faculty;
import org.skypro.javatest.obj.Student;
import org.skypro.javatest.service.FacultyService;
import org.skypro.javatest.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Collection;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {


    @Autowired
    StudentService studentService;

    @Autowired
    FacultyService facultyService;

    @Autowired
    private TestRestTemplate rest;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void deleteAllInDB(){
        studentService.deleteAll();
        facultyService.deleteAll();
    }


    @Test
    public void testGetStudentInfo() {
        Student student = new Student(0, "Jak", 22);
        Faculty faculty = new Faculty(0, "qwerty", "red");
        facultyService.addFaculty(faculty);
        studentService.addStudent(student);
        student.setFacultyStudent(faculty);
        ResponseEntity<Student> response = rest.getForEntity(("http://localhost:" + port + "/student/info/" + student.getId()), Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetStudentInfo_NullStudent() {
        Student student = new Student(0, "ASDF", 22);
        Faculty faculty = new Faculty(0, "qwerty", "red");
        facultyService.addFaculty(faculty);
        student.setFacultyStudent(faculty);
        studentService.addStudent(student);
        studentService.deleteStudent(student.getId());
        ResponseEntity<Student> response = rest.getForEntity("http://localhost:" + port + "/student/info/" + student.getId(), Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetFaculty() {
        Student student = new Student(0, "ASDF", 22);
        Faculty faculty = new Faculty(0, "qwerty", "red");
        facultyService.addFaculty(faculty);
        student.setFacultyStudent(faculty);
        studentService.addStudent(student);
        ResponseEntity<Faculty> response = rest.getForEntity(("http://localhost:" + port + "/student/faculty/" + student.getId()), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(faculty);
    }

    @Test
    public void testGetFaculty_NullFaculty() {
        Student student = new Student(0, "ASDF", 22);
        studentService.addStudent(student);
        ResponseEntity<Faculty> response = rest.getForEntity(("http://localhost:" + port + "/student/faculty/" + student.getId()), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testFindByAge_IsHasThatCollectionStudents() {
        Student student1 = new Student(0, "Dima", 10);
        Student student2 = new Student(0, "Denis", 20);
        studentService.addStudent(student1);
        studentService.addStudent(student2);
        ResponseEntity<Student[]> response = rest.getForEntity(("http://localhost:" + port + "/student/findByAge?min=13&max=25"),
                Student[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getName()).isEqualTo("Denis");
    }

    @Test
    public void testFindByAge_IsHasNotThatCollectionStudents() {
        Student student1 = new Student(0, "Dima", 10);
        Student student2 = new Student(0, "Denis", 20);
        studentService.addStudent(student1);
        studentService.addStudent(student2);
        ResponseEntity<Student[]> response = rest.getForEntity(("http://localhost:" + port + "/student/findByAge?min=25&max=69"),
                Student[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetStudentsOneFaculty_IsHasThatCollectionStudents() {
        Student student1 = new Student(0, "Dima", 10);
        Student student2 = new Student(0, "Denis", 20);
        Faculty faculty = new Faculty(0, "Физмат", "розовый");
        facultyService.addFaculty(faculty);
        student1.setFacultyStudent(faculty);
        student2.setFacultyStudent(faculty);
        studentService.addStudent(student1);
        studentService.addStudent(student2);
        ResponseEntity<Student[]> response = rest.getForEntity(("http://localhost:" + port + "/student/by-faculty?nameFaculty=Физмат"),
                Student[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()[0].getFacultyStudent().getName()).isEqualTo("Физмат");
        assertThat(response.getBody()[1].getFacultyStudent().getName()).isEqualTo("Физмат");


    }

    @Test
    public void testGetStudentsOneFaculty_IsHasNotThatCollectionStudents() {
        Student student1 = new Student(0, "Dima", 10);
        Student student2 = new Student(0, "Denis", 20);
        Faculty faculty = new Faculty(0, "Физмат", "розовый");
        facultyService.addFaculty(faculty);
        student1.setFacultyStudent(faculty);
        student2.setFacultyStudent(faculty);
        studentService.addStudent(student1);
        studentService.addStudent(student2);
        ResponseEntity<Student[]> response = rest.getForEntity(("http://localhost:" + port + "/student/by-faculty?nameFaculty=Химбио}"),
                Student[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCreateStudent_notNull() {
        Student student2 = new Student(0, "Denis", 20);
        ResponseEntity<Student> response = rest.postForEntity(("http://localhost:" + port + "/student/create"), student2,
                Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Denis");
        assertThat(response.getBody().getAge()).isEqualTo(20);
    }

    @Test
    public void testCreateStudent_isNull() {
        ResponseEntity<Student> response = rest.postForEntity(("http://localhost:" + port + "/student/create"), null,
                Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getAge()).isEqualTo(0);
        assertThat(response.getBody().getId()).isEqualTo(0);
        assertThat(response.getBody().getName()).isNull();
    }

    @Test
    public void testEditStudent_StudentIsNotNull(){
        Student student2 = new Student(0, "Denis", 20);
        studentService.addStudent(student2);
        Student student = new Student(0 , "Denis" , 25 );
        ResponseEntity<Student> response = rest.exchange(("http://localhost:" + port + "/student/put"), HttpMethod.PUT , new HttpEntity<>(student) , Student.class );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testEditStudent_StudentIsNull(){
        ResponseEntity<Student> response = rest.exchange(("http://localhost:" + port + "/student/put"), HttpMethod.PUT , HttpEntity.EMPTY, Student.class );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public  void testDeleteStudent_StudentNotNull(){
        Student student2 = new Student(0, "Denis", 20);
      student2 = studentService.addStudent(student2);
        ResponseEntity<Void> response = rest.exchange(("http://localhost:" + port + "/student/delete/" + student2.getId() ), HttpMethod.DELETE , HttpEntity.EMPTY, Void.class );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testDeleteStudent_StudentNull(){
        ResponseEntity<Void> response = rest.exchange(("http://localhost:" + port + "/student/delete/1"), HttpMethod.DELETE , HttpEntity.EMPTY, Void.class );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
