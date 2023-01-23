package pl.schoolmanagementsystem.student.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.schoolmanagementsystem.common.model.AppUser;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.student.dto.CreateStudentDto;
import pl.schoolmanagementsystem.student.dto.StudentSearchDto;
import pl.schoolmanagementsystem.student.dto.StudentWithClassDto;

import java.util.List;

@Mapper(componentModel = "spring", implementationName = "StudentMapperForStudentModuleImp")
public interface StudentMapper {

    @Mapping(source = "appUser", target = "appUser")
    Student mapCreateDtoToEntity(CreateStudentDto createStudentDto, AppUser appUser);

    StudentWithClassDto mapEntityToDtoWithSchoolClass(Student student);

    StudentSearchDto mapEntityToSearchDto(Student student);

    List<StudentSearchDto> mapEntitiesToSearchDtos(List<Student> students);

}
