package pl.schoolmanagementsystem.teacher.utils;

import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.common.user.AppUser;
import pl.schoolmanagementsystem.teacher.dto.CreateTeacherDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.schoolmanagementsystem.common.email.token.TokenGenerator.generateToken;

public class TeacherMapper {

    public static final String PASSWORD = null;

    public static Teacher mapCreateDtoToEntity(CreateTeacherDto createTeacherDto, Set<SchoolSubject> taughtSubjects) {
        return Teacher.builder()
                .name(createTeacherDto.getName())
                .surname(createTeacherDto.getSurname())
                .appUser(createAppUser(createTeacherDto.getEmail()))
                .taughtSubjects(taughtSubjects)
                .teacherInClasses(new HashSet<>())
                .build();
    }

    public static TeacherDto mapEntityToDto(Teacher teacher) {
        return TeacherDto.builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .surname(teacher.getSurname())
                .taughtSubjects(teacher.getTaughtSubjects().stream()
                        .map(SchoolSubject::getName)
                        .collect(Collectors.toSet()))
                .build();
    }

    private static AppUser createAppUser(String email) {
        return new AppUser(email, PASSWORD, generateToken(), new ArrayList<>());
    }
}
