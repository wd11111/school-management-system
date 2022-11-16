package pl.schoolmanagementsystem.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.common.student.StudentRepository;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.common.teacher.TeacherRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final TeacherRepository teacherRepository;

    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Student> student = studentRepository.findByEmail_Email(username);
        if (student.isPresent()) {
            return new User(student.get().getEmail().getEmail(),
                    student.get().getPassword(),
                    getStudentRole());
        }
        Optional<Teacher> teacher = teacherRepository.findByEmail_Email(username);
        if (teacher.isPresent()) {
            return new User(teacher.get().getEmail().getEmail(),
                    teacher.get().getPassword(),
                    getTeacherRoles(teacher.get()));
        }
        throw new UsernameNotFoundException(username);
    }

    private Collection<SimpleGrantedAuthority> getTeacherRoles(Teacher teacher) {
        List<SimpleGrantedAuthority> roles = new ArrayList<>(List.of(new SimpleGrantedAuthority("ROLE_TEACHER")));
        if (teacher.isAdmin()) {
            roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return roles;
    }

    private Collection<SimpleGrantedAuthority> getStudentRole() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_STUDENT"));
    }
}
