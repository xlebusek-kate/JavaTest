package org.skypro.javatest.service;

import org.skypro.javatest.obj.Avatar;
import org.skypro.javatest.obj.Faculty;
import org.skypro.javatest.obj.Student;
import org.skypro.javatest.repository.AvatarRepository;
import org.skypro.javatest.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service

public class StudentService {

    private final String avatarsDir = "./uploads/avatars";
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Optional<Student> findStudent(long id) {
        return studentRepository.findById(id);
    }

    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> findByAge(int one, int two) {
        return studentRepository.findByAgeBetween(one, two);
    }

    public Optional<Faculty> getFacultyById(long idStudent) {
        return studentRepository.findById(idStudent).flatMap(student-> Optional.ofNullable(student.getFacultyStudent()));
    }

    public Collection<Student> getStudentsOneFaculty(String nameFaculty) {
        return studentRepository.findByFacultyStudentNameIgnoreCaseContaining(nameFaculty);
    }

    public Avatar findAvatarById(Long id) {
        return avatarRepository.findByStudentId(id).orElseThrow(IllegalArgumentException::new);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public void uploadAvatar(Long id, MultipartFile file) throws IOException {

        Path filePath = Path.of(avatarsDir, id + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {bis.transferTo(bos);
        }

        Avatar avatar = avatarRepository.findByStudentId(id).orElseGet(Avatar::new);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        avatarRepository.save(avatar);
    }

    public void deleteAll(){
        studentRepository.deleteAll();
    }
}

