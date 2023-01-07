package pl.schoolmanagementsystem.student.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.common.user.AppUser;
import pl.schoolmanagementsystem.common.user.Role;
import pl.schoolmanagementsystem.common.user.RoleRepository;
import pl.schoolmanagementsystem.student.dto.CreateStudentDto;

import java.util.ArrayList;

import static pl.schoolmanagementsystem.common.email.token.TokenGenerator.generateToken;

@Component
@RequiredArgsConstructor
public class StudentCreator {

    private final RoleRepository roleRepository;

    public Student createStudent(CreateStudentDto createStudentDto, SchoolClass schoolClass) {
        Student student = Student.builder()
                .name(createStudentDto.getName())
                .surname(createStudentDto.getSurname())
                .appUser(new AppUser(createStudentDto.getEmail(),
                        null,
                        generateToken(),
                        new ArrayList<>()))
                .schoolClass(schoolClass.getName())
                .build();
        return addRoles(student);
    }

    private Student addRoles(Student student) {
        Role role = roleRepository.findById("ROLE_STUDENT").get();
        student.getAppUser().getRoles().add(role);
        return student;
    }
}
