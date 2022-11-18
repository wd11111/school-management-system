package pl.schoolmanagementsystem.admin.teacher.controller;

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
import pl.schoolmanagementsystem.admin.teacher.mapper.TeacherDtoMapper;
import pl.schoolmanagementsystem.admin.teacher.service.AdminTeacherService;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.common.teacher.dto.TeacherInputDto;
import pl.schoolmanagementsystem.common.teacher.dto.TeacherOutputDto;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static pl.schoolmanagementsystem.admin.teacher.mapper.TeacherDtoMapper.mapToTeacherOutputDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/teachers")
public class AdminTeacherController {

    private final AdminTeacherService adminTeacherService;

    @GetMapping
    public List<TeacherOutputDto> getAllTeachers() {
        return adminTeacherService.getAllTeachers().stream()
                .map(TeacherDtoMapper::mapToTeacherOutputDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/taught-classes")
    public List<SubjectAndClassDto> getTaughtClasses(@PathVariable int id) {
        return adminTeacherService.getTaughtClassesByTeacher(id);
    }

    @PostMapping
    public ResponseEntity<TeacherOutputDto> createTeacher(@RequestBody @Valid TeacherInputDto teacherInputDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapToTeacherOutputDto(adminTeacherService.createTeacher(teacherInputDto)));
    }

    @PatchMapping("/{id}/subjects")
    public TeacherOutputDto addSubjectToTeacher(@PathVariable int id, @RequestBody @Valid SchoolSubjectDto schoolSubjectDto) {
        return mapToTeacherOutputDto(adminTeacherService.addSubjectToTeacher(id, schoolSubjectDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable int id) {
        adminTeacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
