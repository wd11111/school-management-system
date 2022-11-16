package pl.schoolmanagementsystem.admin.schoolClass.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.admin.schoolClass.service.AdminClassService;
import pl.schoolmanagementsystem.common.schoolClass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.common.schoolClass.dto.TeacherInClassInputDto;
import pl.schoolmanagementsystem.common.schoolClass.dto.TeacherInClassOutputDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndTeacherOutputDto;
import pl.schoolmanagementsystem.common.student.dto.StudentOutputDto2;

import java.util.List;

import static pl.schoolmanagementsystem.admin.schoolClass.mapper.TeacherInClassMapper.mapToTeacherInClassOutputDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/classes")
public class AdminClassController {

    private final AdminClassService adminClassService;

    @GetMapping
    public List<SchoolClassDto> getSchoolClasses() {
        return adminClassService.getSchoolClasses();
    }

    @GetMapping("/{className}")
    public List<StudentOutputDto2> getAllStudentsInClass(@PathVariable String className) {
        return adminClassService.getAllStudentsInClass(className);
    }

    @GetMapping("/{className}/subjects")
    public List<SubjectAndTeacherOutputDto> getAllTaughtSubjectsInSchoolClass(@PathVariable String className) {
        return adminClassService.getAllSubjectsInSchoolClass(className);
    }

    @PostMapping
    public ResponseEntity<SchoolClassDto> createSchoolClass(@RequestBody SchoolClassDto schoolClassDto) {
        adminClassService.createSchoolClass(schoolClassDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(schoolClassDto);
    }

    @PatchMapping("/{className}/teachers")
    public ResponseEntity<TeacherInClassOutputDto> addTeacherToSchoolClass(
            @PathVariable String className, @RequestBody TeacherInClassInputDto teacherInClassInputDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapToTeacherInClassOutputDto(adminClassService.addTeacherToSchoolClass(teacherInClassInputDto, className)));
    }

    @DeleteMapping("/{className}")
    public ResponseEntity<Void> deleteSchoolClass(@PathVariable String className) {
        adminClassService.deleteSchoolClass(className);
        return ResponseEntity.noContent().build();
    }
}
