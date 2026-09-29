package org.skypro.javatest.service;

import lombok.extern.slf4j.Slf4j;
import org.skypro.javatest.obj.Faculty;
import org.skypro.javatest.obj.Student;
import org.skypro.javatest.repository.FacultyRepository;
import org.skypro.javatest.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }
    public Faculty addFaculty(Faculty faculty) {
        log.debug("Метод addFaculty с параметром {}", faculty);
        Faculty myFaculty = facultyRepository.save(faculty);
        log.info("Объект Faculty с id {} сохранен", myFaculty.getId());
        return myFaculty;
    }

    public Optional<Faculty> findFaculty(long id) {
        log.debug("Метод findFaculty с параметром id={}", id);
        Optional<Faculty> faculty = facultyRepository.findById(id);
        log.info("Процесс поиска по id={} завершен, найден: {}", id, faculty.isPresent());
        return faculty;
    }

    public Faculty editFaculty(Faculty faculty) {
        log.debug("Метод editFaculty с параметром {}", faculty);
        Faculty faculty1 = facultyRepository.save(faculty);
        log.info("Объект Faculty обновлен: {}", faculty1);
        return faculty1;
    }

    public void deleteFaculty(long id) {
        log.debug("Метод deleteFaculty с параметром id={}", id);
        facultyRepository.deleteById(id);
        log.info("Факультет с id={} удален", id);
    }

    public Faculty findFaculty(String name, String color) {
        log.debug("Метод findFaculty с параметрами name={}, color={}", name, color);
        if (name == null || name.isBlank()) {
            log.warn("Параметр name пустой в findFaculty");
            throw new IllegalArgumentException("name не может быть пустым");
        }
        if (color == null || color.isBlank()) {
            log.warn("Параметр color пустой в findFaculty");
            throw new IllegalArgumentException("color не может быть пустым");
        }
        Faculty result = facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(name, color);
        log.info("Поиск по name={}, color={} завершен, найден: {}", name, color, result);
        return result;
    }

    public Collection<Student> getStudents(String nameFaculty) {
        log.debug("Метод getStudents с параметром nameFaculty={}", nameFaculty);
        Collection<Student> students = studentRepository.findByFacultyStudentNameIgnoreCaseContaining(nameFaculty);
        log.info("Найдено {} студентов для факультета {}", students.size(), nameFaculty);
        return students;
    }

    public void deleteAll() {
        log.warn("Метод deleteAll вызван — будут удалены ВСЕ факультеты!");
        facultyRepository.deleteAll();
        log.info("Все факультеты удалены");
    }

    public Collection<Faculty> getAllFaculties() {
        log.debug("Метод getAllFaculties");
        Collection<Faculty> faculties = facultyRepository.findAll();
        log.info("Найдено {} факультетов", faculties.size());
        return faculties;
    }

    public Collection<Faculty> getAllFacultiesByColor(String color) {
        log.debug("Метод getAllFacultiesByColor с параметром color={}", color);
        if (color == null || color.isBlank()) {
            log.warn("Параметр color пустой в getAllFacultiesByColor");
            throw new IllegalArgumentException("color не может быть пустым");
        }
        Collection<Faculty> result = facultyRepository.findByColorContainingIgnoreCase(color);
        log.info("Найдено {} факультетов по цвету {}", result.size(), color);
        return result;
    }

}
