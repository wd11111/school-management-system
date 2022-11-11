package pl.schoolmanagementsystem.email.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.email.exception.EmailAlreadyInUseException;
import pl.schoolmanagementsystem.email.repository.EmailRepository;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;

    public void checkIfEmailIsAvailable(String email) {
        boolean isEmailAlreadyTaken = emailRepository.existsById(email);
        if (isEmailAlreadyTaken) {
            throw new EmailAlreadyInUseException(email);
        }
    }
}
