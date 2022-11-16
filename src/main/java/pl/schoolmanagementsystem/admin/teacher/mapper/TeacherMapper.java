package pl.schoolmanagementsystem.admin.teacher.mapper;

import pl.schoolmanagementsystem.common.email.Email;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.common.teacher.dto.TeacherInputDto;

import java.util.HashSet;
import java.util.Set;

import static pl.schoolmanagementsystem.admin.mailSender.TokenGenerator.generateToken;

public class TeacherMapper {

    public static Teacher createTeacher(TeacherInputDto teacherInputDto, Set<SchoolSubject> taughtSubjects) {
        return Teacher.builder()
                .name(teacherInputDto.getName())
                .surname(teacherInputDto.getSurname())
                .email(new Email(teacherInputDto.getEmail()))
                .isAdmin(teacherInputDto.isAdmin())
                .password(teacherInputDto.getPassword())
                .taughtSubjects(taughtSubjects)
                .token(generateToken())
                .teacherInClasses(new HashSet<>())
                .build();
    }
}
