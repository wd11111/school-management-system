package pl.schoolmanagementsystem.mapper;

import pl.schoolmanagementsystem.model.Student;
import pl.schoolmanagementsystem.model.dto.output.StudentOutputDto;

public class StudentMapper {

    public static StudentOutputDto mapStudentToDtoOutput(Student student) {
        return new StudentOutputDto(student.getStudentId(), student.getName(),
                student.getSurname(), student.getSchoolClass().getSchoolClassName());
    }
}
