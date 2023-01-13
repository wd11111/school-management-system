package pl.schoolmanagementsystem.common.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.common.teacher.Teacher;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class RoleAdder {

    public static final String ROLE_STUDENT = "ROLE_STUDENT";
    public static final String ROLE_TEACHER = "ROLE_TEACHER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ERROR_MESSAGE = "Failed to add role";

    private final RoleRepository roleRepository;

    public void addRoles(Student student) {
        Role role = roleRepository.findById(ROLE_STUDENT).orElseThrow(() -> new NoSuchElementException(ERROR_MESSAGE));
        student.getAppUser().getRoles().add(role);
    }

    public void addRoles(Teacher teacher, boolean isAdmin) {
        Role roleTeacher = roleRepository.findById(ROLE_TEACHER).orElseThrow(() -> new NoSuchElementException(ERROR_MESSAGE));
        teacher.getAppUser().getRoles().add(roleTeacher);
        if (isAdmin) {
            Role roleAdmin = roleRepository.findById(ROLE_ADMIN).orElseThrow(() -> new NoSuchElementException(ERROR_MESSAGE));
            teacher.getAppUser().getRoles().add(roleAdmin);
        }
    }

}
