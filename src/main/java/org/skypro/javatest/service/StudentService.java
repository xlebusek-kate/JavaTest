package org.skypro.javatest.service;

import lombok.extern.slf4j.Slf4j;
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
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Slf4j
public class StudentService {

    private final String avatarsDir = "./uploads/avatars";
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public Student addStudent(Student student) {
        log.debug("Метод addStudent с параметром {}", student);
        Student saved = studentRepository.save(student);
        log.info("Студент сохранен с id={}", saved.getId());
        return saved;
    }

    public Optional<Student> findStudent(long id) {
        log.debug("Метод findStudent с параметром id={}", id);
        Optional<Student> student = studentRepository.findById(id);
        log.info("Поиск студента по id={} завершен, найден: {}", id, student.isPresent());
        return student;
    }

    public Student editStudent(Student student) {
        log.debug("Метод editStudent с параметром {}", student);
        Student saved = studentRepository.save(student);
        log.info("Студент обновлен: {}", saved);
        return saved;
    }

    public void deleteStudent(long id) {
        log.debug("Метод deleteStudent с параметром id={}", id);
        studentRepository.deleteById(id);
        log.info("Студент с id={} удален", id);
    }

    public Collection<Student> findByAge(int one, int two) {
        log.debug("Метод findByAge с параметрами one={}, two={}", one, two);
        Collection<Student> students = studentRepository.findByAgeBetween(one, two);
        log.info("Найдено {} студентов в возрасте от {} до {}", students.size(), one, two);
        return students;
    }

    public Optional<Faculty> getFacultyById(long idStudent) {
        log.debug("Метод getFacultyById с параметром idStudent={}", idStudent);
        Optional<Faculty> faculty = studentRepository.findById(idStudent)
                .flatMap(student -> Optional.ofNullable(student.getFacultyStudent()));
        log.info("Факультет для студента id={} найден: {}", idStudent, faculty.isPresent());
        return faculty;
    }

    public Collection<Student> getStudentsOneFaculty(String nameFaculty) {
        log.debug("Метод getStudentsOneFaculty с параметром nameFaculty={}", nameFaculty);
        Collection<Student> students = studentRepository.findByFacultyStudentNameIgnoreCaseContaining(nameFaculty);
        log.info("Найдено {} студентов на факультете {}", students.size(), nameFaculty);
        return students;
    }

    public Avatar findAvatarById(Long id) {
        log.debug("Метод findAvatarById с параметром id={}", id);
        return avatarRepository.findByStudentId(id)
                .orElseThrow(() -> {
                    log.warn("Аватар для студента id={} не найден", id);
                    return new IllegalArgumentException("Аватар не найден для id=" + id);
                });
    }

    private String getExtension(String fileName) {
        log.debug("Метод getExtension с параметром fileName={}", fileName);
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        log.debug("Расширение файла: {}", ext);
        return ext;
    }

    public void uploadAvatar(Long id, MultipartFile file) throws IOException {
        log.debug("Метод uploadAvatar с параметрами id={}, fileName={}", id, file.getOriginalFilename());

        Path filePath = Path.of(avatarsDir, id + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {
            bis.transferTo(bos);
        }

        log.info("Файл аватара сохранен на диск: {}", filePath);

        Avatar avatar = avatarRepository.findByStudentId(id).orElseGet(Avatar::new);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        avatarRepository.save(avatar);
        log.info("Аватар для студента id={} сохранен в БД", id);
    }

    public void deleteAll() {
        log.warn("Метод deleteAll вызван — будут удалены ВСЕ студенты!");
        studentRepository.deleteAll();
        log.info("Все студенты удалены");
    }

    public int getAllStudents() {
        log.debug("Метод getAllStudents");
        int count = studentRepository.findAllStudent();
        log.info("Всего студентов: {}", count);
        return count;
    }

    public long getAverageAge() {
        log.debug("Метод getAverageAge");
        long avg = studentRepository.averageAge();
        log.info("Средний возраст студентов: {}", avg);
        return avg;
    }

    public List<Student> getFiveStudentInTheEnd() {
        log.debug("Метод getFiveStudentInTheEnd");
        List<Student> students = studentRepository.findSomeStudent();
        log.info("Найдено {} студентов (последние)", students.size());
        return students;
    }

    public List<String> getStudentsStartingWithA(){
       return studentRepository.findAll()
                .stream()
                .parallel()
                .filter(i-> i.getName().toUpperCase().startsWith(String.valueOf('a').toUpperCase()))
                .map(i-> i.getName().toUpperCase())
                .sorted()
                .toList();
    }

    public int getAverageAgeStudents(){
       return (int) studentRepository.findAll()
                .stream()
                .parallel()
                .mapToInt(Student::getAge)
                .average()
               .orElse(0);
    }

    public String getTheBestLongestFacultyName(){
        return studentRepository.findAll()
                .parallelStream()
                .map(i-> i.getFacultyStudent().getName())
                .max(Comparator.comparing(String::length))
                .orElse("0");
    }

    public long getLong(){
        return LongStream.rangeClosed(1,1000000)
                .parallel()
                .sum();
    }
}

