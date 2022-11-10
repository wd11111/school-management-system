package pl.schoolmanagementsystem.teacher.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import pl.schoolmanagementsystem.email.model.Email;
import pl.schoolmanagementsystem.schoolsubject.service.SchoolSubjectService;
import pl.schoolmanagementsystem.teacher.dto.TeacherInputDto;
import pl.schoolmanagementsystem.teacher.model.Teacher;

import java.util.HashSet;
import java.util.stream.Collectors;

public class TeacherBuilder {

    public static Teacher build(TeacherInputDto teacherInputDto, PasswordEncoder passwordEncoder, SchoolSubjectService schoolSubjectService) {
        return Teacher.builder()
                .name(teacherInputDto.getName())
                .surname(teacherInputDto.getSurname())
                .email(new Email(teacherInputDto.getEmail()))
                .isAdmin(teacherInputDto.isAdmin())
                .password(passwordEncoder.encode(teacherInputDto.getPassword()))
                .taughtSubjects(teacherInputDto.getTaughtSubjects().stream()
                        .map(subject -> schoolSubjectService.findByName(subject)).collect(Collectors.toSet())
                )
                .teacherInClasses(new HashSet<>())
                .build();
    }
}
