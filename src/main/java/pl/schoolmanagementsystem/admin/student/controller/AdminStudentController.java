package pl.schoolmanagementsystem.admin.student.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.admin.student.dto.CreateStudentDto;
import pl.schoolmanagementsystem.admin.student.dto.StudentDto;
import pl.schoolmanagementsystem.admin.student.service.AdminStudentService;

import javax.validation.Valid;

import static pl.schoolmanagementsystem.admin.student.mapper.StudentResponseMapper.mapToStudentResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/students")
public class AdminStudentController {

    private final AdminStudentService adminStudentService;

    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@RequestBody @Valid CreateStudentDto createStudentDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapToStudentResponse(adminStudentService.createStudent(createStudentDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable long id) {
        adminStudentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
