package pl.schoolmanagementsystem.common.password;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PasswordGenerator {
    private final static String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";

    private final PasswordEncoder passwordEncoder;

    public PasswordDto generatePassword() {
        String password = RandomStringUtils.random(12, characters);
        return new PasswordDto(password, passwordEncoder.encode(password));
    }
}
