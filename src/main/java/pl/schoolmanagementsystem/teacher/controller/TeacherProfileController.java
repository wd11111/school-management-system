package pl.schoolmanagementsystem.teacher.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.common.mark.dto.MarkDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.teacher.dto.StudentWithMarksDto;
import pl.schoolmanagementsystem.teacher.service.TeacherProfileService;
import pl.schoolmanagementsystem.teacher.utils.StudentMapper;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherProfileController {

    private final TeacherProfileService teacherProfileService;

    @GetMapping("/classes")
    public Page<SubjectAndClassDto> getTaughtClasses(Principal principal, Pageable pageable) {
        return teacherProfileService.getTaughtClassesByTeacher(principal.getName(), pageable);
    }

    @GetMapping("/classes/{className}")
    public List<StudentWithMarksDto> getStudentsInClass(@PathVariable String className, @RequestParam String subject, Principal principal) {
        return teacherProfileService.getClassStudentsWithMarksOfSubject(className, subject, principal.getName())
                .stream()
                .map(StudentMapper::mapEntityToDtoWithMarks)
                .collect(Collectors.toList());
    }

    @PatchMapping("/students/{id}")
    public ResponseEntity<Void> addMark(@PathVariable long id, @RequestBody @Valid MarkDto markDto, Principal principal) {
        teacherProfileService.addMark(principal.getName(), markDto, id);
        return ResponseEntity.noContent().build();
    }
}
