package pl.schoolmanagementsystem.teacher.utils;

import pl.schoolmanagementsystem.schoolsubject.utils.SchoolSubjectMapper;
import pl.schoolmanagementsystem.teacher.dto.TeacherOutputDto;
import pl.schoolmanagementsystem.teacher.model.Teacher;

import java.util.stream.Collectors;

public class TeacherMapper {

    public static TeacherOutputDto mapTeacherToOutputDto(Teacher teacher) {
        return TeacherOutputDto.builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .surname(teacher.getSurname())
                .taughtSubjects(teacher.getTaughtSubjects().stream()
                        .map(SchoolSubjectMapper::mapSubjectToSubjectDto)
                        .collect(Collectors.toSet()))
                .build();
    }
}
