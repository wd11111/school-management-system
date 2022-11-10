package pl.schoolmanagementsystem.student.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.mark.service.MarkService;
import pl.schoolmanagementsystem.student.service.StudentService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentAccountController {

    private final StudentService studentService;

    private final MarkService markService;

    @Secured("ROLE_STUDENT")
    @GetMapping("/account/averages")
    public ResponseEntity<List<MarkAvgDto>> getAverageMarksForStudent(Principal principal) {
        return ResponseEntity.ok(markService.getAverageMarksForStudent(
                studentService.getIdFromPrincipals(principal)));
    }

    @Secured("ROLE_STUDENT")
    @GetMapping("/account/marks")
    public ResponseEntity<Map<String, List<Integer>>> getGroupedMarksBySubjectForStudent(Principal principal) {
        return ResponseEntity.ok(markService.getGroupedMarksBySubjectForStudent(
                studentService.getIdFromPrincipals(principal)));
    }
}

