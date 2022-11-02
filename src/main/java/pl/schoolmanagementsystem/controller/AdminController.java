package pl.schoolmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.model.*;
import pl.schoolmanagementsystem.model.dto.*;
import pl.schoolmanagementsystem.repository.StudentRepository;
import pl.schoolmanagementsystem.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    private final StudentRepository studentRepository;

    @PostMapping("/classes")
    public ResponseEntity<SchoolClass> createSchoolClass(@RequestBody NameDto schoolClassNameDto) {
        return new ResponseEntity<>(adminService.createSchoolClass(schoolClassNameDto), HttpStatus.CREATED);
    }

    @PostMapping("/students")
    public ResponseEntity<Student> createStudent(@RequestBody StudentDto student) {
        return new ResponseEntity<>(adminService.createStudent(student), HttpStatus.CREATED);
    }

    @PostMapping("/teachers")
    public ResponseEntity<Teacher> createTeacher(@RequestBody TeacherDto teacherDto) {
        return new ResponseEntity<>(adminService.createTeacher(teacherDto), HttpStatus.CREATED);
    }

    @PostMapping("/teachersinclass")
    public ResponseEntity<TeacherInClass> addExistingTeacherToClass(@RequestBody TeacherInClassDto teacherInClassDto) {
        return new ResponseEntity<>(adminService.addExistingTeacherToClass(teacherInClassDto), HttpStatus.CREATED);
    }

    @PostMapping("/subjects")
    public ResponseEntity<SchoolSubject> addSchoolSubject(@RequestBody NameDto subjectNameDto) {
        return new ResponseEntity<>(adminService.addSchoolSubject(subjectNameDto), HttpStatus.CREATED);
    }

    @GetMapping("xd")
    public List<MarkAvg> xd() {
        return studentRepository.findAllAverageMarksForStudentById(1);
    }
}
