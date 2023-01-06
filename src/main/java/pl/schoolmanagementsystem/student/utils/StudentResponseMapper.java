package pl.schoolmanagementsystem.student.utils;

import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.student.dto.StudentDto;

public class StudentResponseMapper {

    public static StudentDto mapToStudentResponse(Student student) {
        return new StudentDto(student.getId(), student.getName(),
                student.getSurname(), student.getSchoolClass());
    }
}
