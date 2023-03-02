package pl.schoolmanagementsystem.teacher.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.teacher.dto.GiveMarkDto;
import pl.schoolmanagementsystem.teacher.dto.MarkStatisticsDto;
import pl.schoolmanagementsystem.teacher.dto.StudentWithMarksDto;
import pl.schoolmanagementsystem.teacher.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.teacher.service.TeacherProfileService;
import pl.schoolmanagementsystem.teacher.service.TeacherStatisticsService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherProfileController {

    private final TeacherProfileService teacherProfileService;
    private final TeacherStatisticsService teacherStatisticsService;

    @GetMapping("/classes")
    public Page<SubjectAndClassDto> getTaughtClasses(Principal principal, Pageable pageable) {
        return teacherProfileService.getTaughtClassesByTeacher(principal.getName(), pageable);
    }

    @GetMapping("/classes/{className}/students")
    public List<StudentWithMarksDto> getStudentsInClass(@PathVariable String className, @RequestParam String subject, Principal principal) {
        return teacherProfileService.getClassStudentsWithMarksOfSubject(className, subject, principal.getName());
    }

    @PostMapping("/students/{id}")
    public ResponseEntity<Void> giveMark(@PathVariable Long id, @RequestBody @Valid GiveMarkDto giveMarkDto, Principal principal) {
        teacherProfileService.giveMark(principal.getName(), giveMarkDto, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics/{subject}")
    public List<MarkStatisticsDto> getStatistics(@PathVariable String subject, Principal principal) {
        return teacherStatisticsService.getStatistics(principal.getName(), subject);
    }

}
