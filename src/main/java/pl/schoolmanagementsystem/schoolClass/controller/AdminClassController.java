package pl.schoolmanagementsystem.schoolClass.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.common.dto.TaughtSubjectDto;
import pl.schoolmanagementsystem.schoolClass.dto.AddOrRemoveTeacherInClassDto;
import pl.schoolmanagementsystem.schoolClass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.schoolClass.dto.StudentDto;
import pl.schoolmanagementsystem.schoolClass.dto.TeacherInClassDto;
import pl.schoolmanagementsystem.schoolClass.service.AdminClassService;
import pl.schoolmanagementsystem.schoolClass.service.AdminTeacherInClassService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/classes")
public class AdminClassController {

    private final AdminClassService adminClassService;

    private final AdminTeacherInClassService teacherInClassService;

    @GetMapping
    public Page<SchoolClassDto> getSchoolClasses(Pageable pageable) {
        return adminClassService.getSchoolClasses(pageable);
    }

    @GetMapping("/{className}/students")
    public List<StudentDto> getAllStudentsInClass(@PathVariable String className) {
        return adminClassService.getAllStudentsInClass(className.toUpperCase());
    }

    @GetMapping("/{className}/subjects")
    public List<TaughtSubjectDto> getAllTaughtSubjectsInSchoolClass(@PathVariable String className) {
        return adminClassService.getTaughtSubjectsInClass(className.toUpperCase());
    }

    @PostMapping
    public ResponseEntity<SchoolClassDto> createSchoolClass(@RequestBody @Valid SchoolClassDto schoolClassDto) {
        adminClassService.createSchoolClass(schoolClassDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(schoolClassDto);
    }

    @PostMapping("/{className}/teachers")
    public TeacherInClassDto assignTeacherToSchoolClass(
            @PathVariable String className, @RequestBody @Valid AddOrRemoveTeacherInClassDto addOrRemoveTeacherInClassDto) {
        return adminClassService.assignTeacherToSchoolClass(addOrRemoveTeacherInClassDto, className.toUpperCase());
    }

    @DeleteMapping("/{className}/teachers")
    public void removeTeacherFromSchoolClass(@PathVariable String className, @RequestBody AddOrRemoveTeacherInClassDto removeTeacherDto) {
        teacherInClassService.removeTeacherFromSchoolClass(removeTeacherDto, className.toUpperCase());
    }

    @DeleteMapping("/{className}")
    public ResponseEntity<Void> deleteSchoolClass(@PathVariable String className) {
        adminClassService.deleteSchoolClass(className.toUpperCase());
        return ResponseEntity.noContent().build();
    }

}
