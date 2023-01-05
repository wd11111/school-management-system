package pl.schoolmanagementsystem.controller;

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
import pl.schoolmanagementsystem.mapper.TeacherDtoMapper;
import pl.schoolmanagementsystem.service.AdminTeacherService;
import pl.schoolmanagementsystem.model.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.model.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.model.dto.TeacherRequestDto;
import pl.schoolmanagementsystem.model.dto.TeacherResponseDto;

import javax.validation.Valid;

import static pl.schoolmanagementsystem.mapper.TeacherDtoMapper.mapToTeacherResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/teachers")
public class AdminTeacherController {

    private final AdminTeacherService adminTeacherService;

    @GetMapping
    public Page<TeacherResponseDto> getAllTeachers(Pageable pageable) {
        return adminTeacherService.getAllTeachers(pageable)
                .map(TeacherDtoMapper::mapToTeacherResponseDto);
    }

    // TODO można zrobić klasę, która  będzie przetrzymywać wszystkie restowe endpointy typu "/{id}/taught-classes", dzięki temu będzie przejrzyście
    @GetMapping("/{id}/taught-classes")
    public Page<SubjectAndClassDto> getTaughtClassesByTeacher(@PathVariable long id, Pageable pageable) {
        return adminTeacherService.getTaughtClassesByTeacher(id, pageable);
    }

    @PostMapping
    public ResponseEntity<TeacherResponseDto> createTeacher(@RequestBody @Valid TeacherRequestDto teacherRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapToTeacherResponseDto(adminTeacherService.createTeacher(teacherRequestDto)));
    }

    @PatchMapping("/{id}/subjects")
    public TeacherResponseDto addSubjectToTeacher(@PathVariable long id, @RequestBody @Valid SchoolSubjectDto schoolSubjectDto) {
        return mapToTeacherResponseDto(adminTeacherService.addSubjectToTeacher(id, schoolSubjectDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable long id) {
        adminTeacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
