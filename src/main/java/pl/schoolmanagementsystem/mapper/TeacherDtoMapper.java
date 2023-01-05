package pl.schoolmanagementsystem.mapper;

import pl.schoolmanagementsystem.model.SchoolSubject;
import pl.schoolmanagementsystem.model.Teacher;
import pl.schoolmanagementsystem.model.dto.TeacherResponseDto;

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
