package pl.schoolmanagementsystem.admin.student.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.common.student.dto.StudentInputDto;
import pl.schoolmanagementsystem.common.user.AppUser;
import pl.schoolmanagementsystem.common.user.Role;
import pl.schoolmanagementsystem.common.user.RoleRepository;

import java.util.ArrayList;

import static pl.schoolmanagementsystem.admin.mailSender.TokenGenerator.generateToken;

@Component
@RequiredArgsConstructor
public class StudentCreator {

    private final RoleRepository roleRepository;

    public Student createStudent(StudentInputDto studentInputDto, SchoolClass schoolClass) {
        Student student = Student.builder()
                .name(studentInputDto.getName())
                .surname(studentInputDto.getSurname())
                .appUser(new AppUser(studentInputDto.getEmail(),
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
