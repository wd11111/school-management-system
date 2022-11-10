package pl.schoolmanagementsystem.teacher.utils;

import pl.schoolmanagementsystem.schoolsubject.model.SchoolSubject;
import pl.schoolmanagementsystem.teacher.model.Teacher;
import pl.schoolmanagementsystem.schoolsubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.teacherinclass.dto.TeacherInClassInputDto;
import pl.schoolmanagementsystem.teacherinclass.dto.TeacherInClassOutputDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherOutputDto;

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
