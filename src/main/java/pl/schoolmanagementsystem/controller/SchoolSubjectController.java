package pl.schoolmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.model.SchoolSubject;
import pl.schoolmanagementsystem.model.dto.TextDto;
import pl.schoolmanagementsystem.service.SchoolSubjectService;

@RestController
@RequiredArgsConstructor
public class SchoolSubjectController {

    private final SchoolSubjectService schoolSubjectService;

    @PostMapping("/subjects")
    public ResponseEntity<SchoolSubject> createSchoolSubject(@RequestBody TextDto subjectTextDto) {
        return new ResponseEntity<>(schoolSubjectService.createSchoolSubject(subjectTextDto), HttpStatus.CREATED);
    }
}
