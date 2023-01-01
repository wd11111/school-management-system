package pl.schoolmanagementsystem.admin.teacher.mapper;

import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.admin.teacher.dto.TeacherResponseDto;

import java.util.stream.Collectors;

public class TeacherDtoMapper {

    public static TeacherResponseDto mapToTeacherResponseDto(Teacher teacher) {
        return TeacherResponseDto.builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .surname(teacher.getSurname())
                .taughtSubjects(teacher.getTaughtSubjects().stream()
                        .map(SchoolSubject::getName)
                        .collect(Collectors.toSet()))
                .build();
    }
}
