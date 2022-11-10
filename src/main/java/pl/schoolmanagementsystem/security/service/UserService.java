package pl.schoolmanagementsystem.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.student.model.Student;
import pl.schoolmanagementsystem.teacher.model.Teacher;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final TeacherUserService teacherUserService;

    private final StudentUserService studentUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Student> student = studentUserService.findStudent(username);
        if (student.isPresent()) {
            return new User(student.get().getEmail().getEmail(),
                    student.get().getPassword(),
                    studentUserService.getStudentRole());
        }
        Optional<Teacher> teacher = teacherUserService.findTeacher(username);
        if (teacher.isPresent()) {
            return new User(teacher.get().getEmail().getEmail(),
                    teacher.get().getPassword(),
                    teacherUserService.getTeacherRoles(teacher.get()));
        }
        throw new UsernameNotFoundException("UNAUTHENTICATED");
    }
}
