package pl.schoolmanagementsystem.common.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.schoolmanagementsystem.common.model.AppUser;
import pl.schoolmanagementsystem.common.model.Mark;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.student.dto.CreateStudentDto;
import pl.schoolmanagementsystem.student.dto.StudentSearchDto;
import pl.schoolmanagementsystem.student.dto.StudentWithClassDto;
import pl.schoolmanagementsystem.teacher.dto.StudentWithMarksDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(source = "appUser", target = "appUser")
    Student mapCreateDtoToEntity(CreateStudentDto createStudentDto, AppUser appUser);

    StudentWithClassDto mapEntityToDtoWithSchoolClass(Student student);

    StudentWithMarksDto mapEntityToDtoWithMarks(Student student);

    default BigDecimal markToBigDecimal(Mark mark) {
        if (mark == null) {
            return null;
        }
        return mark.getMark();
    }

    List<StudentWithMarksDto> mapEntitiesToDtosWithMarks(Set<Student> students);

    StudentSearchDto mapEntityToSearchDto(Student student);

    List<StudentSearchDto> mapEntitiesToSearchDtos(List<Student> students);

}
