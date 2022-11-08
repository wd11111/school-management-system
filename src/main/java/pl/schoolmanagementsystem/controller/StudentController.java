package pl.schoolmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.model.dto.MarkAvgDto;
import pl.schoolmanagementsystem.model.dto.input.MarkInputDto;
import pl.schoolmanagementsystem.model.dto.input.StudentInputDto;
import pl.schoolmanagementsystem.model.dto.output.MarkOutputDto;
import pl.schoolmanagementsystem.model.dto.output.StudentOutputDto;
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

    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}/marks")
    public ResponseEntity<Map<String, List<Integer>>> getGroupedMarksBySubjectForStudent(@PathVariable int id) {
        return ResponseEntity.ok(studentService.getGroupedMarksBySubjectForStudent(id));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}/averages")
    public ResponseEntity<List<MarkAvgDto>> getAverageMarksForStudent(@PathVariable int id) {
        return ResponseEntity.ok(studentService.getAverageMarksForStudent(id));
    }

    @Secured("ROLE_TEACHER")
    @PostMapping("/{id}/marks")
    public ResponseEntity<MarkOutputDto> addMark(@PathVariable int id, @RequestBody MarkInputDto markInputDto,
                                                 @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        return new ResponseEntity<>(teacherService.addMark(authentication.getName(), markInputDto, id), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<StudentOutputDto> createStudent(@RequestBody StudentInputDto studentInputDto) {
        return new ResponseEntity<>(studentService.createStudent(studentInputDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable int id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
