package pl.schoolmanagementsystem.student.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.mark.dto.MarkInputDto;
import pl.schoolmanagementsystem.mark.dto.MarkOutputDto;
import pl.schoolmanagementsystem.mark.service.MarkService;
import pl.schoolmanagementsystem.student.dto.StudentInputDto;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto;
import pl.schoolmanagementsystem.student.service.StudentService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    private final MarkService markService;

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
                                                 Principal principal) {
        return new ResponseEntity<>(markService.addMarkForStudent(principal.getName(), markInputDto, id), HttpStatus.CREATED);
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
