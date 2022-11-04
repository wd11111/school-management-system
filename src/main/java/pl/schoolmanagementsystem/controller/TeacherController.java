package pl.schoolmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.model.Teacher;
import pl.schoolmanagementsystem.model.dto.output.SubjectAndClassOutputDto;
import pl.schoolmanagementsystem.model.dto.input.TeacherInputDto;
import pl.schoolmanagementsystem.model.dto.output.TeacherOutputDto;
import pl.schoolmanagementsystem.service.TeacherService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    public ResponseEntity<Teacher> createTeacher(@RequestBody TeacherInputDto teacherInputDto) {
        return new ResponseEntity<>(teacherService.createTeacher(teacherInputDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TeacherOutputDto>> getAllTeachersInSchool() {
        return ResponseEntity.ok(teacherService.getAllTeachersInSchool());
    }

    @GetMapping("/{id}/classes")
    public ResponseEntity<List<SubjectAndClassOutputDto>> getTaughtClassesByTeacher(@PathVariable int id) {
        return ResponseEntity.ok(teacherService.getTaughtClassesByTeacher(id));
    }
}
