package pl.schoolmanagementsystem.schoolClass.utils;

import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.common.model.TeacherInClass;
import pl.schoolmanagementsystem.schoolClass.dto.TeacherInClassDto;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class TeacherInClassMapper {

    public static TeacherInClassDto mapEntityToDto(TeacherInClass teacherInClass) {
        return TeacherInClassDto.builder()
                .teacherId(teacherInClass.getTeacher().getId())
                .taughtSubject(teacherInClass.getTaughtSubject())
                .taughtClasses(mapClassesToStrings(teacherInClass.getTaughtClasses()))
                .build();
    }

    private static Set<String> mapClassesToStrings(Set<SchoolClass> classes) {
        return classes.stream()
                .map(SchoolClass::getName)
                .collect(toSet());
    }
}
