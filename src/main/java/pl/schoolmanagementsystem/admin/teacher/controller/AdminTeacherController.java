package pl.schoolmanagementsystem.admin.teacher.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import pl.schoolmanagementsystem.admin.teacher.mapper.TeacherDtoMapper;
import pl.schoolmanagementsystem.admin.teacher.service.AdminTeacherService;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.common.teacher.dto.TeacherInputDto;
import pl.schoolmanagementsystem.common.teacher.dto.TeacherOutputDto;

import javax.validation.Valid;

import static pl.schoolmanagementsystem.admin.teacher.mapper.TeacherDtoMapper.mapToTeacherOutputDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/teachers")
public class AdminTeacherController {

    private final AdminTeacherService adminTeacherService;

    @GetMapping
    public Page<TeacherOutputDto> getAllTeachers(Pageable pageable) {
        return adminTeacherService.getAllTeachers(pageable)
                .map(TeacherDtoMapper::mapToTeacherOutputDto);
    }

    @GetMapping("/{id}/taught-classes")
    public Page<SubjectAndClassDto> getTaughtClasses(@PathVariable long id, Pageable pageable) {
        return adminTeacherService.getTaughtClassesByTeacher(id, pageable);
    }

    @PostMapping
    public ResponseEntity<TeacherOutputDto> createTeacher(@RequestBody @Valid TeacherInputDto teacherInputDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapToTeacherOutputDto(adminTeacherService.createTeacher(teacherInputDto)));
    }

    @PatchMapping("/{id}/subjects")
    public TeacherOutputDto addSubjectToTeacher(@PathVariable long id, @RequestBody @Valid SchoolSubjectDto schoolSubjectDto) {
        return mapToTeacherOutputDto(adminTeacherService.addSubjectToTeacher(id, schoolSubjectDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable long id) {
        adminTeacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
