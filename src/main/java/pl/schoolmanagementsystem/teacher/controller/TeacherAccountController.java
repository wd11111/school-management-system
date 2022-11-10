package pl.schoolmanagementsystem.teacher.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.schoolclass.service.SchoolClassService;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto3;
import pl.schoolmanagementsystem.teacher.service.TeacherService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherAccountController {

    private final TeacherService teacherService;

    private final SchoolClassService schoolClassService;

    @Secured("ROLE_TEACHER")
    @GetMapping("/account/classes")
    public ResponseEntity<List<SubjectAndClassOutputDto>> getTaughtClassesByTeacher(Principal principal) {
        return ResponseEntity.ok(teacherService.getTaughtClassesForTeacher(principal.getName()));
    }

    @Secured("ROLE_TEACHER")
    @GetMapping("/{className}/marks/{subjectName}")
    public ResponseEntity<List<StudentOutputDto3>> getAllStudentsInClassWithMarksOfTheSubject(
            @PathVariable String className, @PathVariable String subjectName, Principal principal) {
        return ResponseEntity.ok(schoolClassService.getAllStudentsInClassWithMarksOfTheSubject(
                className, subjectName, principal.getName()));
    }


}
