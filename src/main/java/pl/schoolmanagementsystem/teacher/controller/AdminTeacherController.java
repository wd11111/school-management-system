package pl.schoolmanagementsystem.teacher.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.common.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.teacher.dto.CreateTeacherDto;
import pl.schoolmanagementsystem.teacher.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherDto;
import pl.schoolmanagementsystem.teacher.service.AdminTeacherService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/teachers")
public class AdminTeacherController {

    private final AdminTeacherService adminTeacherService;

    @GetMapping
    public Page<TeacherDto> getAllTeachers(Pageable pageable) {
        return adminTeacherService.getAllTeachers(pageable);
    }

    @GetMapping("/{id}/taught-classes")
    public Page<SubjectAndClassDto> getTaughtClassesByTeacher(@PathVariable Long id, Pageable pageable) {
        return adminTeacherService.getTaughtClassesByTeacher(id, pageable);
    }

    @PostMapping
    public ResponseEntity<TeacherDto> createTeacher(@RequestBody @Valid CreateTeacherDto createTeacherDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminTeacherService.createTeacher(createTeacherDto));
    }

    @PatchMapping("/{id}/subjects")
    public TeacherDto assignSubjectToTeacher(@PathVariable Long id, @RequestBody @Valid SchoolSubjectDto schoolSubjectDto) {
        return adminTeacherService.assignSubjectToTeacher(id, schoolSubjectDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        adminTeacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
