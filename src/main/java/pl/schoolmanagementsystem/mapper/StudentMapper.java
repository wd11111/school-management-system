package pl.schoolmanagementsystem.mapper;

import pl.schoolmanagementsystem.student.model.Student;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto3;

public class StudentMapper {

    public static StudentOutputDto mapStudentToOutputDto(Student student) {
        return new StudentOutputDto(student.getId(), student.getName(),
                student.getSurname(), student.getSchoolClass().getName());
    }

    public static StudentOutputDto3 mapStudentToOutputDto3(Student student) {
        return new StudentOutputDto3(student.getId(), student.getName(),
                student.getSurname(), MarkMapper.mapListOfMarksToIntegers(student.getMarks()));
    }
}
