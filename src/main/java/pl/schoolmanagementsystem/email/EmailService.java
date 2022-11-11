package pl.schoolmanagementsystem.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.email.exception.EmailAlreadyInUseException;

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
