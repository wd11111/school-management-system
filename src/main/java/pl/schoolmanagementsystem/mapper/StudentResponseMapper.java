package pl.schoolmanagementsystem.mapper;

import pl.schoolmanagementsystem.model.Student;
import pl.schoolmanagementsystem.model.StudentResponse;

public class StudentResponseMapper {

    public static StudentResponse mapToStudentResponse(Student student) {
        return new StudentResponse(student.getId(), student.getName(),
                student.getSurname(), student.getSchoolClass());
    }
}
