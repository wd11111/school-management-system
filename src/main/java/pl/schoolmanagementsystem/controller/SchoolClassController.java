package pl.schoolmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.model.dto.SchoolClassDto;
import pl.schoolmanagementsystem.model.dto.input.TeacherInClassInputDto;
import pl.schoolmanagementsystem.model.dto.output.StudentOutputDto2;
import pl.schoolmanagementsystem.model.dto.output.SubjectAndTeacherOutputDto;
import pl.schoolmanagementsystem.model.dto.output.TeacherInClassOutputDto;
import pl.schoolmanagementsystem.service.SchoolClassService;
import pl.schoolmanagementsystem.service.TeacherInClassService;

import javax.annotation.security.RolesAllowed;
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

    @GetMapping("/{className}")
    public ResponseEntity<List<StudentOutputDto2>> getAllStudentsInClass(@PathVariable String className) {
        return ResponseEntity.ok(schoolClassService.getAllStudentsInClass(className));
    }

    @GetMapping("/{className}/subjects")
    public ResponseEntity<List<SubjectAndTeacherOutputDto>> getAllTaughtSubjectsInSchoolClass(@PathVariable String className) {
        return ResponseEntity.ok(schoolClassService.getAllSubjectsForSchoolClass(className));
    }

    @PostMapping
    public ResponseEntity<SchoolClassDto> createSchoolClass(@RequestBody SchoolClassDto schoolClassDto) {
        return new ResponseEntity<>(schoolClassService.createSchoolClass(schoolClassDto), HttpStatus.CREATED);
    }

    @PostMapping("/{className}/teachers")
    public ResponseEntity<TeacherInClassOutputDto> addTeacherToSchoolClass(
            @PathVariable String className, @RequestBody TeacherInClassInputDto teacherInClassInputDto) {
        return new ResponseEntity<>(teacherInClassService.addTeacherInClassToSchoolClass(
                teacherInClassInputDto, className), HttpStatus.CREATED);
    }
}
