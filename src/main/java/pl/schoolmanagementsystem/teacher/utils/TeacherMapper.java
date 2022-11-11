package pl.schoolmanagementsystem.teacher.utils;

import org.springframework.stereotype.Component;
import pl.schoolmanagementsystem.schoolsubject.utils.SchoolSubjectMapper;
import pl.schoolmanagementsystem.teacher.dto.TeacherOutputDto;
import pl.schoolmanagementsystem.teacher.model.Teacher;

import java.util.stream.Collectors;
@Component
public class TeacherMapper {

    public TeacherOutputDto mapTeacherToOutputDto(Teacher teacher) {
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
