package pl.schoolmanagementsystem.teacher.utils;

import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.teacher.dto.TeacherDto;

import java.util.stream.Collectors;

public class TeacherMapper {

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
}
