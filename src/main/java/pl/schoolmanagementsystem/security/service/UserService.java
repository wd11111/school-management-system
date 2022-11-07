package pl.schoolmanagementsystem.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.model.Teacher;
import pl.schoolmanagementsystem.repository.TeacherRepository;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static final String UNAUTHORIZED = "UNAUTHORIZED ";
    private final PasswordEncoder passwordEncoder;
    private final TeacherRepository teacherRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Teacher user = teacherRepository.findByName(username)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException(UNAUTHORIZED);
                });
        return new User(user.getName(), passwordEncoder.encode(user.getSurname()), Collections.emptyList());
    }

}
