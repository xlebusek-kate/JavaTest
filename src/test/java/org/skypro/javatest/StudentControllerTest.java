package org.skypro.javatest;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.skypro.javatest.controller.StudentController;
import org.skypro.javatest.obj.Faculty;
import org.skypro.javatest.obj.Student;
import org.skypro.javatest.repository.StudentRepository;
import org.skypro.javatest.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;


import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = StudentController.class)
public class StudentControllerTest {

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private StudentRepository studentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testGetStudentInfo_NotNull() throws Exception {
        Student student = new Student(1L, "Denis", 20);
        when(studentService.findStudent(anyLong())).thenReturn(Optional.of(student));
        mockMvc.perform(get("/student/info/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Denis")
                )
                .andExpect(jsonPath("$.age").value(20));
    }

    @Test
    public void testGetStudentInfo_Null() throws Exception {
        when(studentService.findStudent(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/student/info/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetFaculty_NotNull() throws Exception {
        Faculty faculty = new Faculty(1L, "Физмат", "Розовый");
        when(studentService.getFaculty(anyLong())).thenReturn(Optional.of(faculty));
        mockMvc.perform(get("/student/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Физмат"))
                .andExpect(jsonPath("$.color").value("Розовый"));
    }

    @Test
    public void testGetFaculty_Null() throws Exception {
        when(studentService.getFaculty(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/student/faculty/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByAge_NotNull() throws Exception {
        Student student1 = new Student(1L, "Denis", 20);
        Student student2 = new Student(2L, "Kate", 19);
        Collection<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        when(studentService.findByAge(anyInt(), anyInt())).thenReturn(students);
        mockMvc.perform(get("/student//findByAge?min=0&max=100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Denis"))
                .andExpect(jsonPath("$[1].name").value("Kate"))
                .andExpect(jsonPath("$[0].age").value(20))
                .andExpect(jsonPath("$[1].age").value(19));
    }

    @Test
    public void testFindByAge_Null() throws Exception {
        when(studentService.findByAge(anyInt(), anyInt())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/student/findByAge?min=0&max=100"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetStudentsOneFaculty_NotNull() throws Exception {
        Student student1 = new Student(1L, "Denis", 20);
        Student student2 = new Student(2L, "Kate", 19);
        Collection<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        when(studentService.getStudentsOneFaculty(anyString())).thenReturn(students);
        mockMvc.perform(get("/student//by-faculty")
                        .param("nameFaculty", "Физмат"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Denis"))
                .andExpect(jsonPath("$[1].name").value("Kate"))
                .andExpect(jsonPath("$[0].age").value(20))
                .andExpect(jsonPath("$[1].age").value(19))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    public void testGetStudentsOneFaculty_Null_BadRequest() throws Exception {
        mockMvc.perform(get("/student//by-faculty")
                        .param("nameFaculty", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetStudentsOneFaculty_Null_NotFound() throws Exception {
        when(studentService.getStudentsOneFaculty(anyString())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/student//by-faculty")
                        .param("nameFaculty", "Физмат"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateStudent_NotNull() throws Exception {
        Student student = new Student(null, "Denis", 20);
        Student savedStudent = new Student(1L, "Denis", 20);
        when(studentService.addStudent(any(Student.class))).thenReturn(savedStudent);
        mockMvc.perform(post("/student/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Denis"))
                .andExpect(jsonPath("$.age").value(20));
    }

    @Test
    public void testCreateStudent_Null() throws Exception {
        mockMvc.perform(post("/student/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEditStudent_NotNull() throws Exception {
      Student student =new Student(1L, "Denis" , 20);
        Student updateStudent = new Student (1L, "Denis" , 33);
      String stringStudent = objectMapper.writeValueAsString(student);
      when(studentService.editStudent(student)).thenReturn(updateStudent);
        mockMvc.perform(put("/student/put")
                .contentType(MediaType.APPLICATION_JSON)
                .content(stringStudent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Denis"))
                .andExpect((jsonPath("$.age")).value(33));
    }

    @Test
    public void testEditStudent_Null() throws Exception {
        mockMvc.perform(put("/student/put")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public  void  testDeleteStudent_NotNull() throws Exception {
        Student student =new Student(1L, "Denis" , 20);
        when( studentService.findStudent(anyLong())).thenReturn(Optional.of(student));
        mockMvc.perform(delete("/student/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    public  void  testDeleteStudent_Null() throws Exception {
        when( studentService.findStudent(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(delete("/student/delete/1"))
                .andExpect(status().isBadRequest());
    }

}

