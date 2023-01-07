package pl.schoolmanagementsystem.schoolClass.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.common.schoolClass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.TaughtSubjectDto;
import pl.schoolmanagementsystem.common.student.dto.StudentDto;
import pl.schoolmanagementsystem.schoolClass.dto.AddTeacherToClassDto;
import pl.schoolmanagementsystem.schoolClass.dto.TeacherInClassDto;
import pl.schoolmanagementsystem.schoolClass.service.AdminClassService;
import pl.schoolmanagementsystem.schoolClass.utils.TeacherInClassMapper;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/classes")
public class AdminClassController {

    private final AdminClassService adminClassService;

    @GetMapping
    public Page<SchoolClassDto> getSchoolClasses(Pageable pageable) {
        System.out.println(pageable.getPageSize());
        return adminClassService.getSchoolClasses(pageable);
    }

    @GetMapping("/{className}")
    public List<StudentDto> getAllStudentsInClass(@PathVariable String className) {
        return adminClassService.getAllStudentsInClass(className);
    }

    @GetMapping("/{className}/subjects")
    public List<TaughtSubjectDto> getAllTaughtSubjectsInSchoolClass(@PathVariable String className) {
        return adminClassService.getTaughtSubjectsInClass(className);
    }

    @PostMapping
    public ResponseEntity<SchoolClassDto> createSchoolClass(@RequestBody @Valid SchoolClassDto schoolClassDto) {
        adminClassService.createSchoolClass(schoolClassDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(schoolClassDto);
    }

    @PatchMapping("/{className}/teachers")
    public TeacherInClassDto addTeacherToSchoolClass(
            @PathVariable String className, @RequestBody @Valid AddTeacherToClassDto addTeacherToClassDto) {
        return TeacherInClassMapper.mapEntityToDto(adminClassService.addTeacherToSchoolClass(addTeacherToClassDto, className));
    }

    @DeleteMapping("/{className}")
    public ResponseEntity<Void> deleteSchoolClass(@PathVariable String className) {
        adminClassService.deleteSchoolClass(className);
        return ResponseEntity.noContent().build();
    }
}
