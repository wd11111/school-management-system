package pl.schoolmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.Model.SchoolClass;
import pl.schoolmanagementsystem.Model.Student;
import pl.schoolmanagementsystem.Model.dto.CreateStudentDto;
import pl.schoolmanagementsystem.Service.AdminService;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/classes")
    public ResponseEntity<SchoolClass> createSchoolClass(@RequestBody String name) {
        return ResponseEntity.ok(adminService.createSchoolClass(name));
    }

    @PostMapping("/students")
    public ResponseEntity<Student> createStudent(@RequestBody CreateStudentDto student) {
        return ResponseEntity.ok(adminService.createStudent(student));
    }
}
