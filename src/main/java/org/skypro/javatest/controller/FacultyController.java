package org.skypro.javatest.controller;

import org.skypro.javatest.obj.Faculty;
import org.skypro.javatest.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    @Autowired
    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<Optional<Faculty>> getFacultyInfo(@PathVariable Long id) {
        if (facultyService.findFaculty(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyService.findFaculty(id));
    }

    @GetMapping("/faculties")
    public ResponseEntity<Collection<Faculty>> findFaculties(@RequestParam(required = false) String color) {
        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(facultyService.findAllFacultiesByColor(color));
        }
        if (facultyService.getAllFaculties().isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }

    @GetMapping("/faculty-whit-param")
    public ResponseEntity<Faculty> findFaculty(@RequestParam String name, @RequestParam String color) {
        if (name == null || name.isBlank() || color == null || color.isBlank())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(facultyService.findFaculty(name, color));
    }

    @PostMapping("/creat")
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        if (faculty == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(facultyService.addFaculty(faculty));
    }

    @PutMapping("/put")
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        if (facultyService.editFaculty(faculty) == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(facultyService.editFaculty(faculty));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        if(facultyService.findFaculty(id).isEmpty()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().build();
    }
}
