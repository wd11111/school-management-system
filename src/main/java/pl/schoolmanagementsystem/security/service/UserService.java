package pl.schoolmanagementsystem.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.common.student.StudentRepository;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.common.teacher.TeacherRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final TeacherRepository teacherRepository;

    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return studentRepository.findByEmail_Email(username)
                .map(student -> new User(student.getName(), student.getPassword(), getStudentRole()))
                .orElseGet(() -> teacherRepository.findByEmail_Email(username)
                        .map(teacher -> new User(teacher.getName(), teacher.getPassword(), getTeacherRoles(teacher)))
                        .orElse(null));
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
