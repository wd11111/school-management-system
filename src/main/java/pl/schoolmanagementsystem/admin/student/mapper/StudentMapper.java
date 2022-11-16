package pl.schoolmanagementsystem.admin.student.mapper;

import pl.schoolmanagementsystem.common.email.Email;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.common.student.dto.StudentInputDto;

import static pl.schoolmanagementsystem.admin.mailSender.TokenGenerator.generateToken;

public class StudentMapper {

    public static Student mapToStudent(StudentInputDto studentInputDto, SchoolClass schoolClass) {
        return Student.builder()
                .name(studentInputDto.getName())
                .surname(studentInputDto.getSurname())
                .email(new Email(studentInputDto.getEmail()))
                .schoolClass(schoolClass.getName())
                .token(generateToken())
                .build();
    }
}
