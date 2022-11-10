package pl.schoolmanagementsystem.teacher.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto3;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto;
import pl.schoolmanagementsystem.schoolclass.service.SchoolClassService;
import pl.schoolmanagementsystem.teacher.service.TeacherService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherAccountController {

    private final TeacherService teacherService;

    private final SchoolClassService schoolClassService;

    @Secured("ROLE_TEACHER")
    @GetMapping("/account/classes")
    public ResponseEntity<List<SubjectAndClassOutputDto>> getTaughtClassesByTeacher(
            @CurrentSecurityContext(expression = "authentication") Authentication authentication
    ) {
        return ResponseEntity.ok(teacherService.getTaughtClassesForTeacher(authentication.getName()));
    }

    @Secured("ROLE_TEACHER")
    @GetMapping("/{className}/marks/{subjectName}")
    public ResponseEntity<List<StudentOutputDto3>> getAllStudentsInClassWithMarksOfTheSubject(
            @PathVariable String className, @PathVariable String subjectName,
            @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        return ResponseEntity.ok(schoolClassService.getAllStudentsInClassWithMarksOfTheSubject(
                className, subjectName, authentication.getName()));
    }


}
