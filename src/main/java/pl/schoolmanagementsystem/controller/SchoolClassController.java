package pl.schoolmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.model.TeacherInClass;
import pl.schoolmanagementsystem.model.dto.TeacherInClassDto;
import pl.schoolmanagementsystem.model.dto.TextDto;
import pl.schoolmanagementsystem.service.SchoolClassService;
import pl.schoolmanagementsystem.service.TeacherInClassService;

@RestController
@RequiredArgsConstructor
public class SchoolClassController {

    private final SchoolClassService schoolClassService;

    private final TeacherInClassService teacherInClassService;

    @PostMapping("/classes")
    public ResponseEntity<SchoolClass> createSchoolClass(@RequestBody TextDto schoolClassTextDto) {
        return new ResponseEntity<>(schoolClassService.createSchoolClass(schoolClassTextDto), HttpStatus.CREATED);
    }

    @PostMapping("/classes/{className}/teachers")
    public ResponseEntity<TeacherInClass> addTeacherInClassToSchoolClass(@PathVariable String className,
                                                                         @RequestBody TeacherInClassDto teacherInClassDto) {
        return new ResponseEntity<>(teacherInClassService.addTeacherInClassToSchoolClass(
                teacherInClassDto, className), HttpStatus.CREATED);
    }

}
