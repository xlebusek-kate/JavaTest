package org.skypro.javatest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skypro.javatest.obj.Faculty;
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


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    StudentService studentService;

    @Autowired
    FacultyService facultyService;

    @Autowired
    private TestRestTemplate rest;

    @BeforeEach
    public void deleteAll() {
        facultyService.deleteAll();
        studentService.deleteAll();
    }

    @Test
    public void testGetFacultyInfo_NotNull() {
        Faculty faculty = new Faculty(0, "Физмат", "Розовый");
        faculty = facultyService.addFaculty(faculty);
        ResponseEntity<Faculty> response = rest.getForEntity(("http://localhost:" + port + "/faculty/info/" + faculty.getId()), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(faculty);
    }

    @Test
    public void testGetFacultyInfo_Null() {
        ResponseEntity<Faculty> response = rest.getForEntity(("http://localhost:" + port + "/faculty/info/1"), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testFindFaculties_NotNull() {
        Faculty faculty1 = new Faculty(0, "A", "666");
        Faculty faculty2 = new Faculty(0, "B", "666");
        Faculty faculty3 = new Faculty(0, "C", "666");
        facultyService.addFaculty(faculty1);
        facultyService.addFaculty(faculty2);
        facultyService.addFaculty(faculty3);
        ResponseEntity<Faculty[]> response = rest.getForEntity(("http://localhost:" + port + "/faculty/faculties?color=666"), Faculty[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(3);
        assertThat(response.getBody()[0].getName()).isEqualTo("A");
        assertThat(response.getBody()[1].getName()).isEqualTo("B");
        assertThat(response.getBody()[2].getName()).isEqualTo("C");
    }

    @Test
    public void testFindFaculties_Null() {
        Faculty faculty1 = new Faculty(0, "A", "666");
        Faculty faculty2 = new Faculty(0, "B", "666");
        Faculty faculty3 = new Faculty(0, "C", "666");
        Faculty faculty4 = new Faculty(0, "DDD", "777");
        Faculty faculty5 = new Faculty(0, "EEE", "888");
        facultyService.addFaculty(faculty1);
        facultyService.addFaculty(faculty2);
        facultyService.addFaculty(faculty3);
        facultyService.addFaculty(faculty4);
        facultyService.addFaculty(faculty5);
        ResponseEntity<Faculty[]> response = rest.getForEntity(("http://localhost:" + port + "/faculty/faculties"), Faculty[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(5);
        assertThat(response.getBody()[0].getName()).isEqualTo("A");
        assertThat(response.getBody()[1].getName()).isEqualTo("B");
        assertThat(response.getBody()[2].getName()).isEqualTo("C");
        assertThat(response.getBody()[3].getName()).isEqualTo("DDD");
        assertThat(response.getBody()[4].getName()).isEqualTo("EEE");
    }

    @Test
    public void testFindFaculty_NotNull() {
        Faculty faculty1 = new Faculty(0, "A", "666");
        facultyService.addFaculty(faculty1);
        ResponseEntity<Faculty> response = rest.getForEntity(("http://localhost:" + port + "/faculty/faculty-whit-param?name=A&color=666"), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(faculty1);
    }

    @Test
    public void testFindFaculty_Null() {
        ResponseEntity<Faculty> response = rest.getForEntity(("http://localhost:" + port + "/faculty/faculty-whit-param?name=A"), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testCreateFaculty_NotNull() {
        Faculty faculty1 = new Faculty(0, "A", "666");
        ResponseEntity<Faculty> response = rest.exchange(("http://localhost:" + port + "/faculty/creat"), HttpMethod.POST, new HttpEntity<>(faculty1), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("A");
        assertThat(response.getBody().getColor()).isEqualTo("666");
    }

    @Test
    public void testCreateFaculty_Null() {
        ResponseEntity<Faculty> response = rest.exchange(("http://localhost:" + port + "/faculty/creat"), HttpMethod.POST, HttpEntity.EMPTY, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testEditFaculty_NotNull() {
        Faculty faculty1 = new Faculty(0, "A", "666");
        faculty1 = facultyService.addFaculty(faculty1);
        Faculty faculty = new Faculty(faculty1.getId(), "AAA", "777");
        ResponseEntity<Faculty> response = rest.exchange(("http://localhost:" + port + "/faculty/put"), HttpMethod.PUT, new HttpEntity<>(faculty), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(faculty);
    }

    @Test
    public void testEditFaculty_Null(){
        ResponseEntity<Faculty> response = rest.exchange(("http://localhost:" + port + "/faculty/put"), HttpMethod.PUT, HttpEntity.EMPTY, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testDeleteFaculty_NotNull(){
        Faculty faculty1 = new Faculty(0, "A", "666");
        faculty1 = facultyService.addFaculty(faculty1);
        ResponseEntity<Faculty> response = rest.exchange(("http://localhost:" + port + "/faculty/delete/"+ faculty1.getId()), HttpMethod.DELETE, HttpEntity.EMPTY, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testDeleteFaculty_Null(){
        ResponseEntity<Faculty> response = rest.exchange(("http://localhost:" + port + "/faculty/delete/1"), HttpMethod.DELETE, HttpEntity.EMPTY, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
