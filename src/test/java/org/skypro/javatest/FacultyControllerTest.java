package org.skypro.javatest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.skypro.javatest.controller.FacultyController;
import org.skypro.javatest.controller.StudentController;
import org.skypro.javatest.obj.Faculty;
import org.skypro.javatest.obj.Student;
import org.skypro.javatest.repository.FacultyRepository;
import org.skypro.javatest.repository.StudentRepository;
import org.skypro.javatest.service.FacultyService;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerTest {

    @MockitoBean
    FacultyService facultyService;

    @MockitoBean
    FacultyRepository facultyRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testGetFacultyInfo_NotNull() throws Exception {
        Faculty faculty = new Faculty(1L, "Физмат", "Розовый");
        when(facultyService.findFaculty(anyLong())).thenReturn(Optional.of(faculty));
        mockMvc.perform(get("/faculty/info/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Физмат"));
    }

    @Test
    public void testGetFacultyInfo_Null() throws Exception {
        when(facultyService.findFaculty(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/faculty/info/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindFaculties_NotNull() throws Exception {
        Collection<Faculty> faculties = new ArrayList<>();
        Faculty faculty = new Faculty(1L, "Физмат", "Розовый");
        faculties.add(faculty);
        when(facultyService.findAllFacultiesByColor(anyString())).thenReturn(faculties);
        mockMvc.perform(get("/faculty/faculties").param("color", "Розовый"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testFindFaculties_Null() throws Exception {
        Collection<Faculty> faculties = new ArrayList<>();
        Faculty faculty = new Faculty(1L, "Физмат", "Розовый");
        faculties.add(faculty);
        when(facultyService.getAllFaculties()).thenReturn(faculties);
        mockMvc.perform(get("/faculty/faculties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testFindFaculties_Null_0() throws Exception {
        Collection<Faculty> faculties = new ArrayList<>();
        when(facultyService.getAllFaculties()).thenReturn(faculties);
        mockMvc.perform(get("/faculty/faculties"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindFaculty_NotNull() throws Exception {
        Faculty faculty = new Faculty(1L, "Физмат", "Розовый");
        when(facultyService.findFaculty(anyString(), anyString())).thenReturn(faculty);
        mockMvc.perform(get("/faculty/faculty-whit-param")
                        .param("name", "123")
                        .param("color", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Физмат"))
                .andExpect(jsonPath("$.color").value("Розовый"));
    }

    @Test
    public void testFindFaculty_Null_isBlank() throws Exception {
        Faculty faculty = new Faculty(1L, "Физмат", "Розовый");
        when(facultyService.findFaculty(anyString(), anyString())).thenReturn(faculty);
        mockMvc.perform(get("/faculty/faculty-whit-param")
                        .param("name", "")
                        .param("color", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFindFaculty_Null_isNull() throws Exception {
        Faculty faculty = new Faculty(1L, "Физмат", "Розовый");
        when(facultyService.findFaculty(anyString(), anyString())).thenReturn(faculty);
        mockMvc.perform(get("/faculty/faculty-whit-param"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateFaculty_NotNull() throws Exception {
        Faculty newFaculty = new Faculty(1L, "Физмат", "Розовый");
        Faculty faculty = new Faculty(null, "Физмат", "Розовый");
        when(facultyService.addFaculty(faculty)).thenReturn(newFaculty);
        mockMvc.perform(post("/faculty/creat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateFaculty_Null() throws Exception {
        mockMvc.perform(post("/faculty/creat"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEditFaculty_NotNull() throws Exception {
        Faculty faculty = new Faculty(1L, "Физмат", "Розовый");
        Faculty newFaculty = new Faculty(null, "Физмат", "Розовый");
        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(newFaculty);
        mockMvc.perform(put("/faculty/put")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk());
    }

    @Test
    public void testEditFaculty_Null() throws Exception {
        mockMvc.perform(put("/faculty/put"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteFaculty_NotNull() throws Exception {
        Faculty faculty = new Faculty(1L, "Физмат", "Розовый");
        when(facultyService.findFaculty(anyLong())).thenReturn(Optional.of(faculty));
        mockMvc.perform(delete("/faculty/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteFaculty_Null() throws Exception {
        Faculty faculty = new Faculty(1L, "Физмат", "Розовый");
        when(facultyService.findFaculty(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(delete("/faculty/delete/1"))
                .andExpect(status().isBadRequest());
    }


}
