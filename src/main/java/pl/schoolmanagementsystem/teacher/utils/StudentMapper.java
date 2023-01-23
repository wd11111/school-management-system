package pl.schoolmanagementsystem.teacher.utils;

import org.mapstruct.Mapper;
import pl.schoolmanagementsystem.common.model.Mark;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.teacher.dto.StudentWithMarksDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", implementationName = "StudentMapperForTeacherModuleImp")
public interface StudentMapper {

    StudentWithMarksDto mapEntityToDtoWithMarks(Student student);

    default BigDecimal markToBigDecimal(Mark mark) {
        if (mark == null) {
            return null;
        }
        return mark.getMark();
    }

    List<StudentWithMarksDto> mapEntitiesToDtosWithMarks(Set<Student> students);
}
