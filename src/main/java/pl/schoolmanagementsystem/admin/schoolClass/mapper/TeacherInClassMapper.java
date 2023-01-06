package pl.schoolmanagementsystem.admin.schoolClass.mapper;

import pl.schoolmanagementsystem.admin.schoolClass.dto.TeacherInClassDto;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.teacher.TeacherInClass;

import static java.util.stream.Collectors.toSet;

public class TeacherInClassMapper {

    public static TeacherInClassDto mapToTeacherInClassResponseDto(TeacherInClass teacherInClass) {
        return TeacherInClassDto.builder()
                .teacherId(teacherInClass.getTeacher().getId())
                .taughtSubject(teacherInClass.getTaughtSubject())
                .schoolClassName(teacherInClass.getTaughtClasses().stream()
                        .map(SchoolClass::getName)
                        .collect(toSet()))
                .build();
    }
}
