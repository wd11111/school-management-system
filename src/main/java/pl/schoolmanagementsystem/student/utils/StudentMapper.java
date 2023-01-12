package pl.schoolmanagementsystem.student.utils;

import pl.schoolmanagementsystem.common.email.token.TokenGenerator;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.common.user.AppUser;
import pl.schoolmanagementsystem.student.dto.CreateStudentDto;
import pl.schoolmanagementsystem.student.dto.StudentWithClassDto;

import java.util.ArrayList;

public class StudentMapper {

    public static final String PASSWORD = null;

    public static Student mapCreateDtoToEntity(CreateStudentDto createStudentDto, SchoolClass schoolClass) {
        return Student.builder()
                .name(createStudentDto.getName())
                .surname(createStudentDto.getSurname())
                .appUser(createAppUser(createStudentDto.getEmail()))
                .schoolClass(schoolClass.getName())
                .build();
    }

    public static StudentWithClassDto mapEntityToDtoWithSchoolClass(Student student) {
        return new StudentWithClassDto(student.getId(), student.getName(), student.getSurname(), student.getSchoolClass());
    }

    private static AppUser createAppUser(String email) {
        return new AppUser(email, PASSWORD, TokenGenerator.generateToken(), new ArrayList<>());
    }
}
