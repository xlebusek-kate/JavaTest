package org.skypro.javatest.service;

import org.skypro.javatest.obj.Faculty;
import org.skypro.javatest.obj.Student;
import org.skypro.javatest.repository.FacultyRepository;
import org.skypro.javatest.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service

public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Optional<Faculty> findFaculty(long id) {
        return facultyRepository.findById(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        facultyRepository.deleteById(id);
    }

    public Faculty findFaculty(String name, String color) {
        if (name == null || name.isBlank() ){
            throw new IllegalArgumentException();
        }
        if (color == null || color.isBlank() ){
            throw new IllegalArgumentException();
        }
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(name, color);
    }


    public Collection<Student> getStudents(String nameFaculty) {
        return studentRepository.findByFacultyStudentNameIgnoreCaseContaining(nameFaculty);
    }

    public void deleteAll(){
        facultyRepository.deleteAll();
    }

    public Collection<Faculty> getAllFaculties(){
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getAllFacultiesByColor(String color){
        if(color== null || color.isBlank()){ throw new IllegalArgumentException();}
         return facultyRepository.findByColorContainingIgnoreCase(color);
    }

}
