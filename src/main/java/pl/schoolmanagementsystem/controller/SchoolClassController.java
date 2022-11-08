package pl.schoolmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.model.dto.SchoolClassDto;
import pl.schoolmanagementsystem.model.dto.input.TeacherInClassInputDto;
import pl.schoolmanagementsystem.model.dto.output.StudentOutputDto2;
import pl.schoolmanagementsystem.model.dto.output.StudentOutputDto3;
import pl.schoolmanagementsystem.model.dto.output.SubjectAndTeacherOutputDto;
import pl.schoolmanagementsystem.model.dto.output.TeacherInClassOutputDto;
import pl.schoolmanagementsystem.service.SchoolClassService;
import pl.schoolmanagementsystem.service.TeacherInClassService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/classes")
public class SchoolClassController {

    private final SchoolClassService schoolClassService;

    private final TeacherInClassService teacherInClassService;

    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<List<SchoolClassDto>> getListOfClasses() {
        return ResponseEntity.ok(schoolClassService.getListOfClasses());
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{className}")
    public ResponseEntity<List<StudentOutputDto2>> getAllStudentsInClass(@PathVariable String className) {
        return ResponseEntity.ok(schoolClassService.getAllStudentsInClass(className));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{className}/marks/{subjectName}")
    public ResponseEntity<List<StudentOutputDto3>> getAllStudentsInClassWithMarksOfTheSubject(
            @PathVariable String className, @PathVariable String subjectName,
            @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        return ResponseEntity.ok(schoolClassService.getAllStudentsInClassWithMarksOfTheSubject(
                className, subjectName, authentication.getName()));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{className}/subjects")
    public ResponseEntity<List<SubjectAndTeacherOutputDto>> getAllTaughtSubjectsInSchoolClass(@PathVariable String className) {
        return ResponseEntity.ok(schoolClassService.getAllSubjectsForSchoolClass(className));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<SchoolClassDto> createSchoolClass(@RequestBody SchoolClassDto schoolClassDto) {
        return new ResponseEntity<>(schoolClassService.createSchoolClass(schoolClassDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/{className}/teachers")
    public ResponseEntity<TeacherInClassOutputDto> addTeacherToSchoolClass(
            @PathVariable String className, @RequestBody TeacherInClassInputDto teacherInClassInputDto) {
        return new ResponseEntity<>(teacherInClassService.addTeacherInClassToSchoolClass(
                teacherInClassInputDto, className), HttpStatus.CREATED);
    }
}
