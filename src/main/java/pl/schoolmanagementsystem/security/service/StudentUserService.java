package pl.schoolmanagementsystem.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.student.Student;
import pl.schoolmanagementsystem.student.StudentRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentUserService {

    private final StudentRepository studentRepository;

    Collection<SimpleGrantedAuthority> getStudentRole() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_STUDENT"));
    }

    Optional<Student> findStudent(String email) {
        return studentRepository.findByEmail_Email(email);
    }
}
