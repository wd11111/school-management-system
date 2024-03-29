package pl.schoolmanagementsystem.student.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.common.criteria.dto.SearchRequestDto;
import pl.schoolmanagementsystem.student.dto.CreateStudentDto;
import pl.schoolmanagementsystem.student.dto.StudentSearchDto;
import pl.schoolmanagementsystem.student.dto.StudentWithClassDto;
import pl.schoolmanagementsystem.student.service.AdminStudentService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/students")
@Validated
public class AdminStudentController {

    private final AdminStudentService adminStudentService;

    @PostMapping
    public ResponseEntity<StudentWithClassDto> createStudent(@RequestBody @Valid CreateStudentDto createStudentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body((adminStudentService.createStudent(createStudentDto)));
    }

    @PostMapping("/search")
    public List<StudentSearchDto> searchStudents(@RequestBody @NotNull @NotEmpty List<SearchRequestDto> searchRequestDtos) {
        return adminStudentService.searchStudent(searchRequestDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        adminStudentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
