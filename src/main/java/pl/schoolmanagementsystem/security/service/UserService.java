package pl.schoolmanagementsystem.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.exception.UserNotFoundException;
import pl.schoolmanagementsystem.model.Student;
import pl.schoolmanagementsystem.model.Teacher;
import pl.schoolmanagementsystem.repository.StudentRepository;
import pl.schoolmanagementsystem.repository.TeacherRepository;

import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static final String UNAUTHORIZED = "UNAUTHORIZED ";

    private final PasswordEncoder passwordEncoder;

    private final TeacherRepository teacherRepository;

    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Student> student = Optional.ofNullable(findStudent(email));
        if (student.isPresent()) {
            return new User(student.get().getEmail().getEmail(),
                    passwordEncoder.encode(student.get().getPassword()),
                    Collections.emptyList());
        }
        Optional<Teacher> teacher = Optional.ofNullable(findTeacher(email));
        if (teacher.isPresent()) {
            return new User(teacher.get().getEmail().getEmail(),
                    passwordEncoder.encode(teacher.get().getPassword()),
                    Collections.emptyList());
        }
        throw new UserNotFoundException();
    }

    private Student findStudent(String email) {
        return studentRepository.findByEmail_Email(email);
    }

    private Teacher findTeacher(String email) {
        return teacherRepository.findByEmail_Email(email);
    }
}
