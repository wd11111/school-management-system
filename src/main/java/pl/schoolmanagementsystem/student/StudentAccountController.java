package pl.schoolmanagementsystem.student;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.mark.MarkFacade;
import pl.schoolmanagementsystem.mark.dto.MarkAvgDto;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentAccountController {

    private final StudentFacade studentFacade;

    private final MarkFacade markFacade;

    @Secured("ROLE_STUDENT")
    @GetMapping("/account/averages")
    public ResponseEntity<List<MarkAvgDto>> getAverageMarksForStudent(Principal principal) {
        int studentId = studentFacade.getIdFromPrincipals(principal);
        return ResponseEntity.ok(markFacade.getAverageMarksForStudent(studentId));
    }

    @Secured("ROLE_STUDENT")
    @GetMapping("/account/marks")
    public ResponseEntity<Map<String, List<Integer>>> getGroupedMarksBySubjectForStudent(Principal principal) {
        int studentId = studentFacade.getIdFromPrincipals(principal);
        return ResponseEntity.ok(markFacade.getGroupedMarksBySubjectForStudent(studentId));
    }
}

