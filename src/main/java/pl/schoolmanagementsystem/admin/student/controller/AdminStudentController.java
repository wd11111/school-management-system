package pl.schoolmanagementsystem.admin.student.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.admin.student.service.AdminStudentService;
import pl.schoolmanagementsystem.admin.student.dto.StudentRequestDto;
import pl.schoolmanagementsystem.admin.student.dto.StudentResponse;

import javax.validation.Valid;

import static pl.schoolmanagementsystem.admin.student.mapper.StudentResponseMapper.mapToStudentResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/students")
public class AdminStudentController {

    private final AdminStudentService adminStudentService;

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@RequestBody @Valid StudentRequestDto studentRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapToStudentResponse(adminStudentService.createStudent(studentRequestDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable long id) {
        adminStudentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
