package pl.schoolmanagementsystem.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto;
import pl.schoolmanagementsystem.student.StudentFacade;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto3;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherAccountController {

    private final TeacherFacade teacherFacade;

    private final StudentFacade studentFacade;

    @Secured("ROLE_TEACHER")
    @GetMapping("/account/classes")
    public ResponseEntity<List<SubjectAndClassOutputDto>> getTaughtClassesByTeacher(Principal principal) {
        int teacherId = teacherFacade.getTeacherIdFromPrincipals(principal);
        return ResponseEntity.ok(teacherFacade.getTaughtClassesByTeacher(teacherId));
    }

    @Secured("ROLE_TEACHER")
    @GetMapping("/{className}/marks")
    public ResponseEntity<List<StudentOutputDto3>> getAllStudentsInClassWithMarksOfTheSubject(
            @PathVariable String className, @RequestParam String subject, Principal principal) {
        int teacherId = teacherFacade.getTeacherIdFromPrincipals(principal);
        return ResponseEntity.ok(studentFacade.getAllStudentsInClassWithMarksOfTheSubject(className, subject, teacherId));
    }
}
