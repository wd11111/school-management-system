package pl.schoolmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.model.dto.SchoolClassDto;
import pl.schoolmanagementsystem.model.dto.input.TeacherInClassInputDto;
import pl.schoolmanagementsystem.model.dto.output.TeacherInClassOutputDto;
import pl.schoolmanagementsystem.service.SchoolClassService;
import pl.schoolmanagementsystem.service.TeacherInClassService;

@RestController
@RequiredArgsConstructor
public class SchoolClassController {

    private final SchoolClassService schoolClassService;

    private final TeacherInClassService teacherInClassService;

    @PostMapping("/classes")
    public ResponseEntity<SchoolClassDto> createSchoolClass(@RequestBody SchoolClassDto schoolClassDto) {
        return new ResponseEntity<>(schoolClassService.createSchoolClass(schoolClassDto), HttpStatus.CREATED);
    }

    @PostMapping("/classes/{className}/teachers")
    public ResponseEntity<TeacherInClassOutputDto> addTeacherToSchoolClass(@PathVariable String className,
                                                                           @RequestBody TeacherInClassInputDto teacherInClassInputDto) {
        return new ResponseEntity<>(teacherInClassService.addTeacherInClassToSchoolClass(
                teacherInClassInputDto, className), HttpStatus.CREATED);
    }

}
