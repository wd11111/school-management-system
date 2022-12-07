package pl.schoolmanagementsystem.teacher.mapper;

import pl.schoolmanagementsystem.common.mark.MarkMapper;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.common.student.dto.StudentResponseDto3;

public class StudentDtoMapper {

    public static StudentResponseDto3 mapToStudentResponseDto3(Student student) {
        return new StudentResponseDto3(
                student.getId(),
                student.getName(),
                student.getSurname(),
                MarkMapper.mapListOfMarksToBytes(student.getMarks()));
    }
}
