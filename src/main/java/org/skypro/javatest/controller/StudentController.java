package org.skypro.javatest.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.skypro.javatest.obj.Avatar;
import org.skypro.javatest.obj.Faculty;
import org.skypro.javatest.obj.Student;
import org.skypro.javatest.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.findStudent(id).orElseThrow(()-> new RuntimeException("Не найдено")));
    }

    @GetMapping("/faculty/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable long id) {
        return ResponseEntity.ok(studentService.getFaculty(id));
    }

    @GetMapping("/findByAge")
    public ResponseEntity<Collection<Student>> findByAge(@RequestParam int min, @RequestParam int max) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.findByAge(min, max));
    }

    @GetMapping("/by-faculty")
    public ResponseEntity<Collection<Student>> getStudentsOneFaculty(@RequestParam String nameFaculty){
        return ResponseEntity.ok(studentService.getStudentsOneFaculty(nameFaculty));
    }

    @PostMapping("/create")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.addStudent(student));
    }

    @PostMapping(value = "/avatar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String>uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar){
        if (avatar.getSize() > 1024 * 300) {
            return ResponseEntity.badRequest().body("File is too big");
        }
        try {
            studentService.uploadAvatar(id, avatar);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }


    @PutMapping("/put")
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.editStudent(student));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatars(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() > 1024 * 300) {
            return ResponseEntity.badRequest().body("File is too big");
        }
        studentService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/avatar/preview")
    public ResponseEntity<byte[]>downloadAvatar(@PathVariable Long id) {
        Avatar avatar = studentService.findAvatarById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping(value = "/{id}/avatar")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = studentService.findAvatarById(id);

        Path path = Path.of(avatar.getFilePath());

        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream()) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }
}

