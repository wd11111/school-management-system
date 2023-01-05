package pl.schoolmanagementsystem.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.model.Student;
import pl.schoolmanagementsystem.model.dto.StudentRequestDto;
import pl.schoolmanagementsystem.model.AppUser;
import pl.schoolmanagementsystem.model.Role;
import pl.schoolmanagementsystem.repository.RoleRepository;

import java.util.ArrayList;

import static pl.schoolmanagementsystem.utils.TokenGenerator.generateToken;

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
