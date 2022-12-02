package pl.schoolmanagementsystem.admin.schoolClass.mapper;

import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.schoolClass.dto.TeacherInClassResponseDto;
import pl.schoolmanagementsystem.common.teacher.TeacherInClass;

import static java.util.stream.Collectors.toSet;

public class TeacherInClassMapper {

    public static TeacherInClassResponseDto mapToTeacherInClassResponseDto(TeacherInClass teacherInClass) {
        return TeacherInClassResponseDto.builder()
                .teacherId(teacherInClass.getTeacher().getId())
                .taughtSubject(teacherInClass.getTaughtSubject())
                .schoolClassName(teacherInClass.getTaughtClasses().stream()
                        .map(SchoolClass::getName)
                        .collect(toSet()))
                .build();
    }
}
