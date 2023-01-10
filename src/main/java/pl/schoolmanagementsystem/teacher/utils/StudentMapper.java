package pl.schoolmanagementsystem.teacher.utils;

import pl.schoolmanagementsystem.common.mark.Mark;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.teacher.dto.StudentWithMarksDto;

import java.util.List;
import java.util.stream.Collectors;

public class StudentMapper {

    public static StudentWithMarksDto mapEntityToDtoWithMarks(Student student) {
        return new StudentWithMarksDto(
                student.getId(),
                student.getName(),
                student.getSurname(),
                mapListOfMarksToDoubles(student.getMarks()));
    }

    private static List<Double> mapListOfMarksToDoubles(List<Mark> marks) {
        return marks.stream()
                .map(Mark::getMark)
                .collect(Collectors.toList());
    }
}
