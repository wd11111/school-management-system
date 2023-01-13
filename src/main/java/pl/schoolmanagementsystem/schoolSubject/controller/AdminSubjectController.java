package pl.schoolmanagementsystem.schoolSubject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.common.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.schoolSubject.service.AdminSubjectService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/subjects")
public class AdminSubjectController {

    private final AdminSubjectService adminSubjectService;

    @GetMapping
    public Page<SchoolSubjectDto> getSchoolSubjects(Pageable pageable) {
        return adminSubjectService.getAllSubjects(pageable);
    }

    @PostMapping
    public ResponseEntity<SchoolSubjectDto> createSchoolSubject(@RequestBody @Valid SchoolSubjectDto schoolSubjectDto) {
        adminSubjectService.createSchoolSubject(schoolSubjectDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(schoolSubjectDto);
    }

    @DeleteMapping("/{subjectName}")
    public ResponseEntity<Void> deleteSchoolSubject(@PathVariable String subjectName) {
        adminSubjectService.deleteSchoolSubject(subjectName);
        return ResponseEntity.noContent().build();
    }
}
