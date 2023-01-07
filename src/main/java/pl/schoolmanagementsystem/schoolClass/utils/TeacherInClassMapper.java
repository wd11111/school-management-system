package pl.schoolmanagementsystem.schoolClass.utils;

import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.teacher.TeacherInClass;
import pl.schoolmanagementsystem.schoolClass.dto.TeacherInClassDto;

import static java.util.stream.Collectors.toSet;

public class TeacherInClassMapper {

    public static TeacherInClassDto mapEntityToDto(TeacherInClass teacherInClass) {
        return TeacherInClassDto.builder()
                .teacherId(teacherInClass.getTeacher().getId())
                .taughtSubject(teacherInClass.getTaughtSubject())
                .schoolClassName(teacherInClass.getTaughtClasses().stream()
                        .map(SchoolClass::getName)
                        .collect(toSet()))
                .build();
    }
}
