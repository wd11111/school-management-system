package pl.schoolmanagementsystem.student.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.schoolmanagementsystem.common.model.AppUser;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.student.dto.CreateStudentDto;

@Mapper(componentModel = "spring")
public interface StudentMapperMapstruct {

    @Mapping(source = "appUser", target = "appUser")
    Student mapCreateDtoToEntity(CreateStudentDto createStudentDto, AppUser appUser);

}
