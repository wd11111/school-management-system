package pl.schoolmanagementsystem.student.utils;

import org.mapstruct.Mapper;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.student.dto.StudentWithClassDto;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentWithClassDto mapEntityToDtoWithSchoolClass(Student student);

}
