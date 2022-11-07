package pl.schoolmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.model.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.service.SchoolSubjectService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subjects")
public class SchoolSubjectController {

    private final SchoolSubjectService schoolSubjectService;

    @GetMapping
    public ResponseEntity<List<SchoolSubjectDto>> getAllSchoolSubjects() {
        return ResponseEntity.ok(schoolSubjectService.getAllSchoolSubjects());
    }

    @PostMapping
    public ResponseEntity<SchoolSubjectDto> createSchoolSubject(@RequestBody SchoolSubjectDto schoolSubjectDto) {
        return new ResponseEntity<>(schoolSubjectService.createSchoolSubject(schoolSubjectDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{subjectName}")
    public ResponseEntity<Void> deleteSchoolSubject(@PathVariable SchoolSubjectDto schoolSubjectDto) {
        schoolSubjectService.deleteSchoolSubjectByName(schoolSubjectDto);
        return ResponseEntity.noContent().build();
    }
}
