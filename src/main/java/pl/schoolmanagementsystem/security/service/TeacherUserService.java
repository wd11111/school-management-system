package pl.schoolmanagementsystem.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.common.teacher.TeacherRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class TeacherUserService {

    private final TeacherRepository teacherRepository;

    Collection<SimpleGrantedAuthority> getTeacherRoles(Teacher teacher) {
        List<SimpleGrantedAuthority> roles = new ArrayList<>(List.of(new SimpleGrantedAuthority("ROLE_TEACHER")));
        if (teacher.isAdmin()) {
            roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return roles;
    }

    Optional<Teacher> findTeacher(String email) {
        return teacherRepository.findByEmail_Email(email);
    }

}
