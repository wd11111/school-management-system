package pl.schoolmanagementsystem.teacher.utils;

import pl.schoolmanagementsystem.common.model.Mark;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.teacher.dto.StudentWithMarksDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class StudentMapperStub implements StudentMapper {

    @Override
    public StudentWithMarksDto mapEntityToDtoWithMarks(Student student) {
        return new StudentWithMarksDto(
                student.getId(),
                student.getName(),
                student.getSurname(),
                student.getMarks().stream()
                        .map(this::markToBigDecimal)
                        .toList());
    }

    @Override
    public List<StudentWithMarksDto> mapEntitiesToDtosWithMarks(Set<Student> students) {
        return students.stream()
                .map(this::mapEntityToDtoWithMarks)
                .toList();
    }

    @Override
    public BigDecimal markToBigDecimal(Mark mark) {
        return StudentMapper.super.markToBigDecimal(mark);
    }
}
