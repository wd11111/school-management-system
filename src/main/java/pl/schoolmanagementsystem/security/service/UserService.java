package pl.schoolmanagementsystem.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.model.Student;
import pl.schoolmanagementsystem.model.Teacher;
import pl.schoolmanagementsystem.repository.StudentRepository;
import pl.schoolmanagementsystem.repository.TeacherRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final TeacherRepository teacherRepository;

    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Student> student = findStudent(username);
        if (student.isPresent()) {
            return new User(student.get().getEmail().getEmail(),
                    student.get().getPassword(),
                    getStudentRole());
        }
        Optional<Teacher> teacher = findTeacher(username);
        if (teacher.isPresent()) {
            return new User(teacher.get().getEmail().getEmail(),
                    teacher.get().getPassword(),
                    getTeacherRoles(teacher.get()));
        }
        throw new UsernameNotFoundException("UNAUTHENTICATED");
    }

    private Optional<Student> findStudent(String email) {
        return studentRepository.findByEmail_Email(email);
    }

    private Optional<Teacher> findTeacher(String email) {
        return teacherRepository.findByEmail_Email(email);
    }

    private Collection<SimpleGrantedAuthority> getStudentRole() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_STUDENT"));
    }

    private Collection<SimpleGrantedAuthority> getTeacherRoles(Teacher teacher) {
        List<SimpleGrantedAuthority> roles = new ArrayList<>(List.of(new SimpleGrantedAuthority("ROLE_TEACHER")));
        if (teacher.isAdmin()) {
            roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return roles;
    }
}
