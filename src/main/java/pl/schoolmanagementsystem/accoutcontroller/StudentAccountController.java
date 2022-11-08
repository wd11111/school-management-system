package pl.schoolmanagementsystem.accoutcontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.model.dto.MarkAvgDto;
import pl.schoolmanagementsystem.service.StudentService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentAccountController {

    private final StudentService studentService;

    @Secured("ROLE_STUDENT")
    @GetMapping("/account/averages")
    public ResponseEntity<List<MarkAvgDto>> getAverageMarksForStudent(
            @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        return ResponseEntity.ok(studentService.getAveragesForStudentAccount(authentication.getName()));
    }

    @Secured("ROLE_STUDENT")
    @GetMapping("/account/marks")
    public ResponseEntity<Map<String, List<Integer>>> getGroupedMarksBySubjectForStudent(
            @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        return ResponseEntity.ok(studentService.getGroupedMarksBySubjectForStudentAccount(authentication.getName()));
    }
}
