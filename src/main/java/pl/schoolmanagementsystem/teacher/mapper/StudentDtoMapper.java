package pl.schoolmanagementsystem.teacher.mapper;

import pl.schoolmanagementsystem.common.mark.MarkMapper;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.common.student.dto.StudentOutputDto3;

public class StudentDtoMapper {

    public static StudentOutputDto3 mapToStudentOutputDto3(Student student) {
        return new StudentOutputDto3(
                student.getId(),
                student.getName(),
                student.getSurname(),
                MarkMapper.mapListOfMarksToBytes(student.getMarks()));
    }
}
