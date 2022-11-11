package pl.schoolmanagementsystem.teacher.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.schoolmanagementsystem.email.Email;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubject;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubjectService;
import pl.schoolmanagementsystem.teacher.dto.TeacherInputDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherOutputDto;
import pl.schoolmanagementsystem.teacher.Teacher;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TeacherMapper {

    private final PasswordEncoder passwordEncoder;

    private final SchoolSubjectService schoolSubjectService;

    public TeacherOutputDto mapTeacherToOutputDto(Teacher teacher) {
        return TeacherOutputDto.builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .surname(teacher.getSurname())
                .taughtSubjects(teacher.getTaughtSubjects().stream()
                        .map(SchoolSubject::getName)
                        .collect(Collectors.toSet()))
                .build();
    }

    public Teacher mapInputDtoToTeacher(TeacherInputDto teacherInputDto) {
        return Teacher.builder()
                .name(teacherInputDto.getName())
                .surname(teacherInputDto.getSurname())
                .email(new Email(teacherInputDto.getEmail()))
                .isAdmin(teacherInputDto.isAdmin())
                .password(passwordEncoder.encode(teacherInputDto.getPassword()))
                .taughtSubjects(teacherInputDto.getTaughtSubjects().stream()
                        .map(schoolSubjectService::findByName).collect(Collectors.toSet())
                )
                .teacherInClasses(new HashSet<>())
                .build();
    }
}
