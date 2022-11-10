package pl.schoolmanagementsystem.schoolclass.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.schoolclass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.schoolclass.service.SchoolClassService;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndTeacherOutputDto;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto2;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto3;
import pl.schoolmanagementsystem.teacherinclass.dto.TeacherInClassInputDto;
import pl.schoolmanagementsystem.teacherinclass.dto.TeacherInClassOutputDto;
import pl.schoolmanagementsystem.teacherinclass.service.TeacherInClassService;

import java.security.Principal;
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
            @PathVariable String className, @PathVariable String subjectName, Principal principal) {
        return ResponseEntity.ok(schoolClassService.getAllStudentsInClassWithMarksOfTheSubject(
                className, subjectName, principal.getName()));
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
