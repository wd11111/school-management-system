package pl.schoolmanagementsystem.email.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.exception.EmailAlreadyInUseException;
import pl.schoolmanagementsystem.email.repository.EmailRepository;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;

    public boolean isEmailAvailable(String email) {
        return emailRepository.existsById(email);
    }

    public void checkIfEmailIsAvailable(String email) {
        if (isEmailAvailable(email)) {
            throw new EmailAlreadyInUseException(email);
        }
    }
}
