package pl.schoolmanagementsystem.admin.student.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.admin.student.dto.StudentRequestDto;
import pl.schoolmanagementsystem.common.user.AppUser;
import pl.schoolmanagementsystem.common.user.Role;
import pl.schoolmanagementsystem.common.user.RoleRepository;

import java.util.ArrayList;

import static pl.schoolmanagementsystem.admin.common.mail.TokenGenerator.generateToken;

@Component
@RequiredArgsConstructor
public class StudentCreator {

    private final RoleRepository roleRepository;

    public Student createStudent(StudentRequestDto studentRequestDto, SchoolClass schoolClass) {
        Student student = Student.builder()
                .name(studentRequestDto.getName())
                .surname(studentRequestDto.getSurname())
                .appUser(new AppUser(studentRequestDto.getEmail(),
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
