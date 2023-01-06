package pl.schoolmanagementsystem.admin.student.mapper;

import pl.schoolmanagementsystem.admin.student.dto.StudentDto;
import pl.schoolmanagementsystem.common.student.Student;

public class StudentResponseMapper {

    public static StudentDto mapToStudentResponse(Student student) {
        return new StudentDto(student.getId(), student.getName(),
                student.getSurname(), student.getSchoolClass());
    }
}
