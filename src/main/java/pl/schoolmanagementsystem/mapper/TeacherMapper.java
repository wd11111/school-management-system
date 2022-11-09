package pl.schoolmanagementsystem.mapper;

import pl.schoolmanagementsystem.model.SchoolSubject;
import pl.schoolmanagementsystem.model.Teacher;
import pl.schoolmanagementsystem.model.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.model.dto.input.TeacherInClassInputDto;
import pl.schoolmanagementsystem.model.dto.output.TeacherInClassOutputDto;
import pl.schoolmanagementsystem.model.dto.output.TeacherOutputDto;

import java.util.Set;
import java.util.stream.Collectors;

public class TeacherMapper {

    public static TeacherOutputDto mapTeacherToOutputDto(Teacher teacher) {
        return TeacherOutputDto.builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .surname(teacher.getSurname())
                .taughtSubjects(mapListOfSubjectsToListOfSubjectDto(teacher.getTaughtSubjects()))
                .build();
    }

    public static Set<SchoolSubjectDto> mapListOfSubjectsToListOfSubjectDto(Set<SchoolSubject> subjects) {
        return subjects.stream().map(subject -> new SchoolSubjectDto(subject.getName()))
                .collect(Collectors.toSet());
    }

    public static TeacherInClassOutputDto mapTeacherInClassInputToOutputDto(
            TeacherInClassInputDto teacherInput, String subjectName) {
        return new TeacherInClassOutputDto(
                teacherInput.getTeacherId(), teacherInput.getTaughtSubject(), subjectName);
    }
}
