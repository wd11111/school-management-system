package pl.schoolmanagementsystem.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.common.security.CouldNotConfirmIUserException;
import pl.schoolmanagementsystem.common.security.PasswordsDoNotMatchException;
import pl.schoolmanagementsystem.common.student.StudentRepository;
import pl.schoolmanagementsystem.common.teacher.TeacherRepository;
import pl.schoolmanagementsystem.security.dto.PasswordDto;

@Service
@RequiredArgsConstructor
public class ConfirmAccountService {

    private final TeacherRepository teacherRepository;

    private final StudentRepository studentRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void confirm(PasswordDto passwordDto, String token) {
        if (!doesPasswordsMatch(passwordDto)) {
            throw new PasswordsDoNotMatchException();
        }
        studentRepository.findByToken(token)
                .ifPresentOrElse(student -> student.setPassword(passwordEncoder.encode(passwordDto.getPassword())),
                        () -> teacherRepository.findByToken(token)
                                .ifPresentOrElse(teacher -> teacher.setPassword(passwordEncoder.encode(passwordDto.getPassword())),
                                        CouldNotConfirmIUserException::new));
    }

    private boolean doesPasswordsMatch(PasswordDto passwordDto) {
        return passwordDto.getPassword().equals(passwordDto.getConfirmPassword());
    }
}
