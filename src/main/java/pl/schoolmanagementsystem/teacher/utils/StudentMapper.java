package pl.schoolmanagementsystem.teacher.utils;

import org.mapstruct.Mapper;
import pl.schoolmanagementsystem.common.model.Mark;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.teacher.dto.StudentWithMarksDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentWithMarksDto mapEntityToDtoWithMarks(Student student);

    List<StudentWithMarksDto> mapEntitiesToDtosWithMarks(Set<Student> students);

    default BigDecimal markToBigDecimal(Mark mark) {
        return mark.getMark();
    }

}
