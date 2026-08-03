package org.skypro.javatest.controller;

import org.skypro.javatest.obj.Faculty;
import org.skypro.javatest.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
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
        Optional<Faculty> faculty = facultyService.findFaculty(id);
        if (facultyService.findFaculty(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(facultyService.findFaculty(id));
    }

    @GetMapping("/faculty")
    public ResponseEntity<Collection<Faculty>> findFaculties(@RequestParam(required = false) String color) {
        if (color != null && !color.isBlank()) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/faculty-whit-param")
    public  ResponseEntity<Faculty> findFaculty(@RequestParam String name, @RequestParam String color){
        return ResponseEntity.status(HttpStatus.OK).body(facultyService.findFaculty(name,color));
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultyService.editFaculty(faculty);
        if (foundFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }
}
