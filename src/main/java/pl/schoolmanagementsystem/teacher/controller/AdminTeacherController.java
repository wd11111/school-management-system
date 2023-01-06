package pl.schoolmanagementsystem.teacher.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.teacher.dto.CreateTeacherDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherDto;
import pl.schoolmanagementsystem.teacher.service.AdminTeacherService;
import pl.schoolmanagementsystem.teacher.utils.TeacherDtoMapper;

import javax.validation.Valid;

import static pl.schoolmanagementsystem.teacher.utils.TeacherDtoMapper.mapToTeacherResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/teachers")
public class AdminTeacherController {

    private final AdminTeacherService adminTeacherService;

    @GetMapping
    public Page<TeacherDto> getAllTeachers(Pageable pageable) {
        return adminTeacherService.getAllTeachers(pageable)
                .map(TeacherDtoMapper::mapToTeacherResponseDto);
    }

    @GetMapping("/{id}/taught-classes")
    public Page<SubjectAndClassDto> getTaughtClassesByTeacher(@PathVariable long id, Pageable pageable) {
        return adminTeacherService.getTaughtClassesByTeacher(id, pageable);
    }

    @PostMapping
    public ResponseEntity<TeacherDto> createTeacher(@RequestBody @Valid CreateTeacherDto createTeacherDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapToTeacherResponseDto(adminTeacherService.createTeacher(createTeacherDto)));
    }

    @PatchMapping("/{id}/subjects")
    public TeacherDto addSubjectToTeacher(@PathVariable long id, @RequestBody @Valid SchoolSubjectDto schoolSubjectDto) {
        return mapToTeacherResponseDto(adminTeacherService.addSubjectToTeacher(id, schoolSubjectDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable long id) {
        adminTeacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
