package pl.schoolmanagementsystem.student.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import pl.schoolmanagementsystem.email.model.Email;
import pl.schoolmanagementsystem.schoolclass.model.SchoolClass;
import pl.schoolmanagementsystem.student.dto.StudentInputDto;
import pl.schoolmanagementsystem.student.model.Student;

public class StudentBuilder {

    public static Student buildStudent(StudentInputDto studentInputDto, SchoolClass schoolClass, PasswordEncoder passwordEncoder) {
        return Student.builder()
                .name(studentInputDto.getName())
                .surname(studentInputDto.getSurname())
                .email(new Email(studentInputDto.getEmail()))
                .password(passwordEncoder.encode(studentInputDto.getPassword()))
                .schoolClass(schoolClass)
                .build();
    }
}
