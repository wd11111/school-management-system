package pl.schoolmanagementsystem.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.common.user.AppUserRepository;
import pl.schoolmanagementsystem.security.dto.PasswordDto;
import pl.schoolmanagementsystem.security.exception.CouldNotConfirmUserException;
import pl.schoolmanagementsystem.security.exception.PasswordsDoNotMatchException;

@Service
@RequiredArgsConstructor
public class ConfirmAccountService {

    private final PasswordEncoder passwordEncoder;

    private final AppUserRepository userRepository;

    @Transactional
    public void confirmAccount(PasswordDto passwordDto, String token) {
        if (!doPasswordsMatch(passwordDto)) {
            throw new PasswordsDoNotMatchException();
        }
        userRepository.findByToken(token)
                .ifPresentOrElse(appUser -> appUser.setPassword(passwordEncoder.encode(passwordDto.getPassword())),
                        () -> {
                            throw new CouldNotConfirmUserException();
                        });
    }

    private boolean doPasswordsMatch(PasswordDto passwordDto) {
        return passwordDto.getPassword().equals(passwordDto.getConfirmPassword());
    }
}
