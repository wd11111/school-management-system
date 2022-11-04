package pl.schoolmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.model.Teacher;
import pl.schoolmanagementsystem.model.dto.SubjectClassDto;
import pl.schoolmanagementsystem.model.dto.TeacherDto;
import pl.schoolmanagementsystem.service.TeacherService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    public ResponseEntity<Teacher> createTeacher(@RequestBody TeacherDto teacherDto) {
        return new ResponseEntity<>(teacherService.createTeacher(teacherDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachersInSchool() {
        return ResponseEntity.ok(teacherService.getAllTeachersInSchool());
    }

    @GetMapping("/{id}/classes")
    public ResponseEntity<List<SubjectClassDto>> getTaughtClassesByTeacher(@PathVariable int id) {
        return ResponseEntity.ok(teacherService.getTaughtClassesByTeacher(id));
    }
}
