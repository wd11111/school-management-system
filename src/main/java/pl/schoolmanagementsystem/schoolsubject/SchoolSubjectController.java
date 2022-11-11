package pl.schoolmanagementsystem.schoolsubject;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.schoolsubject.dto.SchoolSubjectDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subjects")
public class SchoolSubjectController {

    private final SchoolSubjectService schoolSubjectService;

    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<List<SchoolSubjectDto>> getAllSchoolSubjects() {
        return ResponseEntity.ok(schoolSubjectService.getAllSchoolSubjects());
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<SchoolSubjectDto> createSchoolSubject(@RequestBody SchoolSubjectDto schoolSubjectDto) {
        return new ResponseEntity<>(schoolSubjectService.createSchoolSubject(schoolSubjectDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{subjectName}")
    public ResponseEntity<Void> deleteSchoolSubject(@PathVariable String subjectName) {
        schoolSubjectService.deleteSchoolSubjectByName(subjectName);
        return ResponseEntity.noContent().build();
    }
}
