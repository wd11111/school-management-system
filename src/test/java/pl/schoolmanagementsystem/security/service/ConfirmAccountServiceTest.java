package pl.schoolmanagementsystem.security.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.schoolmanagementsystem.common.security.CouldNotConfirmUserException;
import pl.schoolmanagementsystem.common.security.PasswordsDoNotMatchException;
import pl.schoolmanagementsystem.common.user.AppUser;
import pl.schoolmanagementsystem.common.user.AppUserRepository;
import pl.schoolmanagementsystem.security.dto.PasswordDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfirmAccountServiceTest {

    public static final String PASSWORD = "aaa";
    public static final String NOT_MATCHING_PASSWORD = "bbb";
    public static final String TOKEN = "qwertyuiop";

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AppUserRepository userRepository;

    @InjectMocks
    private ConfirmAccountService confirmAccountService;

    @Test
    void should_confirm_account() {
        AppUser appUser = new AppUser();
        PasswordDto passwordDto = new PasswordDto(PASSWORD, PASSWORD);
        when(userRepository.findByToken(anyString())).thenReturn(Optional.of(appUser));
        when(passwordEncoder.encode(anyString())).thenReturn(PASSWORD);

        confirmAccountService.confirmAccount(passwordDto, TOKEN);

        assertThat(appUser.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    void should_throw_exception_when_passwords_dont_match() {
        AppUser appUser = new AppUser();
        PasswordDto passwordDto = new PasswordDto(PASSWORD, NOT_MATCHING_PASSWORD);

        assertThatThrownBy(() -> confirmAccountService.confirmAccount(passwordDto, TOKEN))
                .isInstanceOf(PasswordsDoNotMatchException.class)
                .hasMessage("Passwords do not match!");
        assertThat(appUser.getPassword()).isNull();
    }

    @Test
    void should_throw_exception_when_user_not_found() {
        AppUser appUser = new AppUser();
        PasswordDto passwordDto = new PasswordDto(PASSWORD, PASSWORD);
        when(userRepository.findByToken(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> confirmAccountService.confirmAccount(passwordDto, TOKEN))
                .isInstanceOf(CouldNotConfirmUserException.class)
                .hasMessage("Could not confirm user!");
        assertThat(appUser.getPassword()).isNull();
    }

}