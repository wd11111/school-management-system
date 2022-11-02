package pl.schoolmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.Model.*;
import pl.schoolmanagementsystem.Model.dto.CreateStudentDto;
import pl.schoolmanagementsystem.Model.dto.CreateTeacherDto;
import pl.schoolmanagementsystem.Model.dto.TeacherInClassDto;
import pl.schoolmanagementsystem.Service.AdminService;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/classes")
    public ResponseEntity<SchoolClass> createSchoolClass(@RequestBody String name) {
        return new ResponseEntity<>(adminService.createSchoolClass(name), HttpStatus.CREATED);
    }

    @PostMapping("/students")
    public ResponseEntity<Student> createStudent(@RequestBody CreateStudentDto student) {
        return new ResponseEntity<>(adminService.createStudent(student), HttpStatus.CREATED);
    }

    @PostMapping("/teachers")
    public ResponseEntity<Teacher> createTeacher(@RequestBody CreateTeacherDto createTeacherDto) {
        return new ResponseEntity<>(adminService.createTeacher(createTeacherDto), HttpStatus.CREATED);
    }

    @PostMapping("/teachersinclass")
    public ResponseEntity<TeacherInClass> addExistingTeacherToClass(@RequestBody TeacherInClassDto teacherInClassDto) {
        return new ResponseEntity<>(adminService.addExistingTeacherToClass(teacherInClassDto), HttpStatus.CREATED);
    }

    @PostMapping("/subjects")
    public ResponseEntity<SchoolSubject> addSchoolSubject(@RequestBody String subjectName) {
        return new ResponseEntity<>(adminService.addSchoolSubject(subjectName), HttpStatus.CREATED);
    }
}
