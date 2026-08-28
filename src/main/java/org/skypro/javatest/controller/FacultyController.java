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
        if (facultyService.findFaculty(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(facultyService.findFaculty(id));
    }

    @GetMapping("/faculties")
    public ResponseEntity<Collection<Faculty>> findFaculties(@RequestParam(required = false) String color) {
        if (color != null && !color.isBlank()) {
            return ResponseEntity.status(HttpStatus.OK).body(facultyService.getAllFacultiesByColor(color));
        }else return ResponseEntity.status(HttpStatus.OK).body(facultyService.getAllFaculties());
    }

    @GetMapping("/faculty-whit-param")
    public  ResponseEntity<Faculty> findFaculty(@RequestParam String name, @RequestParam String color){
        if (name == null ||name.isBlank() || color== null ||color.isBlank()){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(facultyService.findFaculty(name,color));
    }

    @PostMapping("/creat")
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        if(faculty == null) {
        ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(facultyService.addFaculty(faculty));
    }

    @PutMapping("/put")
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultyService.editFaculty(faculty);
        if (foundFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        if(facultyService.findFaculty(id).isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }
}
