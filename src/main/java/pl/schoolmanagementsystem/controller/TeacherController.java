package pl.schoolmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.model.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.model.dto.input.TeacherInputDto;
import pl.schoolmanagementsystem.model.dto.output.SubjectAndClassOutputDto;
import pl.schoolmanagementsystem.model.dto.output.TeacherOutputDto;
import pl.schoolmanagementsystem.service.TeacherService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public ResponseEntity<List<TeacherOutputDto>> getAllTeachersInSchool() {
        return ResponseEntity.ok(teacherService.getAllTeachersInSchool());
    }

    @GetMapping("/{id}/classes")
    public ResponseEntity<List<SubjectAndClassOutputDto>> getTaughtClassesByTeacher(@PathVariable int id) {
        return ResponseEntity.ok(teacherService.getTaughtClassesByTeacher(id));
    }

    @PostMapping
    public ResponseEntity<TeacherOutputDto> createTeacher(@RequestBody TeacherInputDto teacherInputDto) {
        return new ResponseEntity<>(teacherService.createTeacher(teacherInputDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/classes")
    public ResponseEntity<TeacherOutputDto> addTaughtSubjectToTeacher(@PathVariable int id,
                                                                      @RequestBody SchoolSubjectDto schoolSubjectDto) {
        return ResponseEntity.ok(teacherService.addTaughtSubjectToTeacher(id, schoolSubjectDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable int id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
