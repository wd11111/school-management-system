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
public class StudentMapper {

    private final RoleRepository roleRepository;

    public Student mapToStudent(StudentInputDto studentInputDto, SchoolClass schoolClass) {
        Student student = Student.builder()
                .name(studentInputDto.getName())
                .surname(studentInputDto.getSurname())
                .appUser(new AppUser(studentInputDto.getEmail(),
                        null,
                        generateToken(),
                        new ArrayList<>()))
                .schoolClass(schoolClass.getName())
                .build();
        Role role = roleRepository.findById("ROLE_STUDENT").get();
        student.getAppUser().getRoles().add(role);
        return student;
    }
}
