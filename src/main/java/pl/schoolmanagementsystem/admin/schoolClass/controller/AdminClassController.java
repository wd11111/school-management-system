package pl.schoolmanagementsystem.admin.schoolClass.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.admin.schoolClass.dto.TeacherInClassRequestDto;
import pl.schoolmanagementsystem.admin.schoolClass.dto.TeacherInClassResponseDto;
import pl.schoolmanagementsystem.admin.schoolClass.service.AdminClassService;
import pl.schoolmanagementsystem.common.schoolClass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndTeacherResponseDto;
import pl.schoolmanagementsystem.common.student.dto.StudentResponseDto2;

import javax.validation.Valid;
import java.util.List;

import static pl.schoolmanagementsystem.admin.schoolClass.mapper.TeacherInClassMapper.mapToTeacherInClassResponseDto;

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
    public List<StudentResponseDto2> getAllStudentsInClass(@PathVariable String className) {
        return adminClassService.getAllStudentsInClass(className);
    }

    @GetMapping("/{className}/subjects")
    public List<SubjectAndTeacherResponseDto> getAllTaughtSubjectsInSchoolClass(@PathVariable String className) {
        return adminClassService.getTaughtSubjectsInClass(className);
    }

    @PostMapping
    public ResponseEntity<SchoolClassDto> createSchoolClass(@RequestBody @Valid SchoolClassDto schoolClassDto) {
        adminClassService.createSchoolClass(schoolClassDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(schoolClassDto);
    }

    @PatchMapping("/{className}/teachers")
    public TeacherInClassResponseDto addTeacherToSchoolClass(
            @PathVariable String className, @RequestBody @Valid TeacherInClassRequestDto teacherInClassRequestDto) {
        return mapToTeacherInClassResponseDto(adminClassService.addTeacherToSchoolClass(teacherInClassRequestDto, className));
    }

    @DeleteMapping("/{className}")
    public ResponseEntity<Void> deleteSchoolClass(@PathVariable String className) {
        adminClassService.deleteSchoolClass(className);
        return ResponseEntity.noContent().build();
    }
}
