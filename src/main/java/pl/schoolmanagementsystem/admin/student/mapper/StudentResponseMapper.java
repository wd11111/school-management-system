package pl.schoolmanagementsystem.admin.student.mapper;

import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.common.student.dto.StudentResponse;

public class StudentResponseMapper {

    public static StudentResponse mapToStudentResponse(Student student) {
        return new StudentResponse(student.getId(), student.getName(),
                student.getSurname(), student.getSchoolClass());
    }
}
