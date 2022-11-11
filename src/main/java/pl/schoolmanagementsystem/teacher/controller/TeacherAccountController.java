package pl.schoolmanagementsystem.teacher.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto3;
import pl.schoolmanagementsystem.student.service.StudentService;
import pl.schoolmanagementsystem.teacher.model.Teacher;
import pl.schoolmanagementsystem.teacher.service.TeacherService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherAccountController {

    private final TeacherService teacherService;

    private final StudentService studentService;

    @Secured("ROLE_TEACHER")
    @GetMapping("/account/classes")
    public ResponseEntity<List<SubjectAndClassOutputDto>> getTaughtClassesByTeacher(Principal principal) {
        int idFromPrincipals = teacherService.getIdFromPrincipals(principal);
        return ResponseEntity.ok(teacherService.getTaughtClassesByTeacher(idFromPrincipals));
    }

    @Secured("ROLE_TEACHER")
    @GetMapping("/{className}/marks")
    public ResponseEntity<List<StudentOutputDto3>> getAllStudentsInClassWithMarksOfTheSubject(
            @PathVariable String className, @RequestParam String subject, Principal principal) {
        int idFromPrincipals = teacherService.getIdFromPrincipals(principal);
        return ResponseEntity.ok(studentService.getAllStudentsInClassWithMarksOfTheSubject(
                className, subject, idFromPrincipals));
    }


}
