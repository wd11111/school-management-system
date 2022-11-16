package pl.schoolmanagementsystem.admin.schoolClass.mapper;

import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.schoolClass.dto.TeacherInClassOutputDto;
import pl.schoolmanagementsystem.common.teacher.TeacherInClass;

import static java.util.stream.Collectors.toSet;

public class TeacherInClassMapper {

    public static TeacherInClassOutputDto mapToTeacherInClassOutputDto(TeacherInClass teacherInClass) {
        return TeacherInClassOutputDto.builder()
                .teacherId(teacherInClass.getTeacher().getId())
                .taughtSubject(teacherInClass.getTaughtSubject())
                .schoolClassName(teacherInClass.getTaughtClasses().stream()
                        .map(SchoolClass::getName)
                        .collect(toSet()))
                .build();
    }
}
