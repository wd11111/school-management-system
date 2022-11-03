package pl.schoolmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.model.dto.MarkAvg;
import pl.schoolmanagementsystem.service.StudentService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/{studentId}/marks")
    public ResponseEntity<Map<String, List<Integer>>> getGroupedMarksBySubjectForStudentById(@PathVariable int studentId) {
        return ResponseEntity.ok(studentService.getGroupedMarksBySubjectForStudent(studentId));
    }

    @GetMapping("/{studentId}/averagemarks")
    public List<MarkAvg> getAllAverageMarksForStudentById(@PathVariable int studentId) {
        return studentService.getAllAverageMarksForStudentById(studentId);
    }
}
