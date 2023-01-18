package pl.schoolmanagementsystem.schoolClass.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.common.model.TeacherInClass;
import pl.schoolmanagementsystem.schoolClass.dto.TeacherInClassDto;

@Mapper(componentModel = "spring")
public interface TeacherInClassMapper {

    @Mapping(source = "teacherInClass.teacher.id", target = "teacherId")
    TeacherInClassDto mapEntityToDto(TeacherInClass teacherInClass);

    default String schoolClassToString(SchoolClass schoolClass) {
        return schoolClass.getName();
    }
}
