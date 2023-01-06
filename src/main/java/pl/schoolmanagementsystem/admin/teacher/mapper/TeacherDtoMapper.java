package pl.schoolmanagementsystem.admin.teacher.mapper;

import pl.schoolmanagementsystem.admin.teacher.dto.TeacherDto;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.teacher.Teacher;

import java.util.stream.Collectors;

public class TeacherDtoMapper {

    public static TeacherDto mapToTeacherResponseDto(Teacher teacher) {
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
