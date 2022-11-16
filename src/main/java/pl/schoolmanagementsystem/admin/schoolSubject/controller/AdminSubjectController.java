package pl.schoolmanagementsystem.admin.schoolSubject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.admin.schoolSubject.service.AdminSubjectService;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/subjects")
public class AdminSubjectController {

    private final AdminSubjectService adminSubjectService;

    @GetMapping
    public List<SchoolSubjectDto> getSchoolSubjects() {
        return adminSubjectService.getAllSubjects();
    }

    @PostMapping
    public ResponseEntity<SchoolSubjectDto> createSchoolSubject(@RequestBody SchoolSubjectDto schoolSubjectDto) {
        adminSubjectService.createSchoolSubject(schoolSubjectDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(schoolSubjectDto);
    }

    @DeleteMapping("/{subjectName}")
    public ResponseEntity<Void> deleteSchoolSubject(@PathVariable String subjectName) {
        adminSubjectService.deleteSchoolSubject(subjectName);
        return ResponseEntity.noContent().build();
    }
}
