package pl.schoolmanagementsystem.teacher.utils;

import pl.schoolmanagementsystem.common.mark.Mark;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.teacher.dto.StudentResponseDto3;

import java.util.List;
import java.util.stream.Collectors;

public class StudentDtoMapper {

    public static StudentResponseDto3 mapToStudentResponseDto3(Student student) {
        return new StudentResponseDto3(
                student.getId(),
                student.getName(),
                student.getSurname(),
                mapListOfMarksToBytes(student.getMarks()));
    }

    private static List<Byte> mapListOfMarksToBytes(List<Mark> marks) {
        return marks.stream()
                .map(Mark::getMark)
                .collect(Collectors.toList());
    }
}
