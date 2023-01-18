package pl.schoolmanagementsystem.teacher.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.schoolmanagementsystem.common.model.AppUser;
import pl.schoolmanagementsystem.common.model.SchoolSubject;
import pl.schoolmanagementsystem.common.model.Teacher;
import pl.schoolmanagementsystem.teacher.dto.CreateTeacherDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherDto;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    @Mapping(source = "taughtSubjects", target = "taughtSubjects")
    @Mapping(source = "appUser", target = "appUser")
    Teacher mapCreateDtoToEntity(CreateTeacherDto createTeacherDto, Set<SchoolSubject> taughtSubjects, AppUser appUser);

    TeacherDto mapEntityToDto(Teacher teacher);

    default String schoolSubjectToString(SchoolSubject schoolSubject) {
        return schoolSubject.getName();
    }

}
