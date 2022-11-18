package pl.schoolmanagementsystem.teacher.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.common.mark.dto.MarkInputDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.common.student.dto.StudentOutputDto3;
import pl.schoolmanagementsystem.teacher.mapper.StudentDtoMapper;
import pl.schoolmanagementsystem.teacher.service.TeacherClassService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherClassController {

    private final TeacherClassService teacherClassService;

    @GetMapping("/classes")
    public Page<SubjectAndClassDto> getTaughtClasses(Principal principal, Pageable pageable) {
        return teacherClassService.getTaughtClassesByTeacher(principal.getName(), pageable);
    }

    @GetMapping("/classes/{className}")
    public List<StudentOutputDto3> getStudentsInClass(@PathVariable String className, @RequestParam String subject, Principal principal) {
        return teacherClassService.getAllStudentsInClassWithMarksOfSubject(className, subject, principal.getName())
                .stream()
                .map(StudentDtoMapper::mapToStudentOutputDto3)
                .collect(Collectors.toList());
    }

    @PatchMapping("/students/{id}")
    public ResponseEntity<Void> addMark(@PathVariable int id, @RequestBody @Valid MarkInputDto markInputDto, Principal principal) {
        teacherClassService.addMark(principal.getName(), markInputDto, id);
        return ResponseEntity.noContent().build();
    }
}
