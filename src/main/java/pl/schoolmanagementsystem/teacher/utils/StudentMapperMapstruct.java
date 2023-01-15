package pl.schoolmanagementsystem.teacher.utils;

import org.mapstruct.Mapper;
import pl.schoolmanagementsystem.common.model.Mark;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.teacher.dto.StudentWithMarksDto;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface StudentMapperMapstruct {

    StudentWithMarksDto mapEntityToDtoWithMarks(Student student);

    default BigDecimal markToBigDecimal(Mark mark) {
        return mark.getMark();
    }

}
