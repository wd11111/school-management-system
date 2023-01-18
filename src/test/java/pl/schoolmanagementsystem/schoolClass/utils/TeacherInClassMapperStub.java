package pl.schoolmanagementsystem.schoolClass.utils;

import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.common.model.TeacherInClass;
import pl.schoolmanagementsystem.schoolClass.dto.TeacherInClassDto;

import static java.util.stream.Collectors.toSet;

public class TeacherInClassMapperStub implements TeacherInClassMapper {

    @Override
    public TeacherInClassDto mapEntityToDto(TeacherInClass teacherInClass) {
        return TeacherInClassDto.builder()
                .teacherId(teacherInClass.getTeacher().getId())
                .taughtSubject(teacherInClass.getTaughtSubject())
                .taughtClasses(teacherInClass.getTaughtClasses().stream()
                        .map(this::schoolClassToString)
                        .collect(toSet()))
                .build();
    }

    @Override
    public String schoolClassToString(SchoolClass schoolClass) {
        return schoolClass.getName();
    }
}
