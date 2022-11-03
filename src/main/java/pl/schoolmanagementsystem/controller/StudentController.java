package pl.schoolmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.model.Mark;
import pl.schoolmanagementsystem.model.dto.MarkAvgDto;
import pl.schoolmanagementsystem.model.dto.MarkDto;
import pl.schoolmanagementsystem.service.AdminService;
import pl.schoolmanagementsystem.service.StudentService;
import pl.schoolmanagementsystem.service.TeacherService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    private final TeacherService teacherService;

    private final AdminService adminService;

    @GetMapping("/{id}/marks")
    public ResponseEntity<Map<String, List<Integer>>> getGroupedMarksBySubjectForStudent(@PathVariable int id) {
        return ResponseEntity.ok(studentService.getGroupedMarksBySubjectForStudent(id));
    }

    @GetMapping("/{id}/averagemarks")
    public ResponseEntity<List<MarkAvgDto>> getAllAverageMarksForStudent(@PathVariable int id) {
        return ResponseEntity.ok(studentService.getAllAverageMarksForStudentById(id));
    }

    @PostMapping("/{id}/marks")
    public ResponseEntity<Mark> addMark(@PathVariable int id, @RequestBody MarkDto markDto) {
        return new ResponseEntity<>(teacherService.addMark(markDto), HttpStatus.CREATED);
    }
/*
    @PostMapping()
    public ResponseEntity<Student> createStudent(@RequestBody StudentDto student) {
        return new ResponseEntity<>(adminService.createStudent(student), HttpStatus.CREATED);
    }*/
}
