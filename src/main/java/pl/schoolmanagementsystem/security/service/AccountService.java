package pl.schoolmanagementsystem.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.common.email.service.EmailSender;
import pl.schoolmanagementsystem.common.model.AppUser;
import pl.schoolmanagementsystem.common.repository.AppUserRepository;
import pl.schoolmanagementsystem.security.dto.PasswordDto;
import pl.schoolmanagementsystem.security.exception.CouldNotConfirmUserException;

import static pl.schoolmanagementsystem.common.email.token.TokenGenerator.generateToken;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository userRepository;
    private final EmailSender emailSender;

    @Transactional
    public void confirmPassword(PasswordDto passwordDto, String token) {
        userRepository.findByToken(token)
                .ifPresentOrElse(appUser -> {
                            appUser.setPassword(passwordEncoder.encode(passwordDto.password()));
                            appUser.setToken(null);
                        },
                        () -> {
                            throw new CouldNotConfirmUserException();
                        });
    }

    public void resetPassword(String email) {
        AppUser user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setToken(generateToken());
        emailSender.sendEmail(email, user.getToken());

        userRepository.save(user);
    }

}
