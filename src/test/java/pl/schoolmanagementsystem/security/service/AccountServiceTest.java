package pl.schoolmanagementsystem.security.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.schoolmanagementsystem.common.email.service.EmailSender;
import pl.schoolmanagementsystem.common.model.AppUser;
import pl.schoolmanagementsystem.common.repository.AppUserRepository;
import pl.schoolmanagementsystem.security.dto.PasswordDto;
import pl.schoolmanagementsystem.security.exception.CouldNotConfirmUserException;
import pl.schoolmanagementsystem.security.exception.PasswordsDoNotMatchException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    public static final String PASSWORD = "aaa";
    public static final String NOT_MATCHING_PASSWORD = "bbb";
    public static final String TOKEN = "abc";
    public static final String EMAIL = "Email@";

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private EmailSender emailSender;

    @InjectMocks
    private AccountService accountService;

    @Test
    void should_confirm_account() {
        AppUser appUser = new AppUser();
        PasswordDto passwordDto = new PasswordDto(PASSWORD, PASSWORD);
        when(userRepository.findByToken(anyString())).thenReturn(Optional.of(appUser));
        when(passwordEncoder.encode(anyString())).thenReturn(PASSWORD);

        accountService.confirmPassword(passwordDto, TOKEN);

        assertThat(appUser.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    void should_throw_exception_when_passwords_dont_match() {
        AppUser appUser = new AppUser();
        PasswordDto passwordDto = new PasswordDto(PASSWORD, NOT_MATCHING_PASSWORD);

        assertThatThrownBy(() -> accountService.confirmPassword(passwordDto, TOKEN))
                .isInstanceOf(PasswordsDoNotMatchException.class)
                .hasMessage("Passwords do not match!");
        assertThat(appUser.getPassword()).isNull();
    }

    @Test
    void should_throw_exception_when_user_not_found() {
        AppUser appUser = new AppUser();
        PasswordDto passwordDto = new PasswordDto(PASSWORD, PASSWORD);
        when(userRepository.findByToken(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.confirmPassword(passwordDto, TOKEN))
                .isInstanceOf(CouldNotConfirmUserException.class)
                .hasMessage("Could not confirm user!");
        assertThat(appUser.getPassword()).isNull();
    }

    @Test
    void should_correctly_reset_password() {
        AppUser appUser = new AppUser();
        appUser.setUserEmail(EMAIL);
        when(userRepository.findByUserEmail(anyString())).thenReturn(Optional.of(appUser));

        accountService.resetPassword(EMAIL);

        assertThat(appUser.getToken()).isNotNull();
        verify(emailSender, times(1)).sendEmail(EMAIL, appUser.getToken());
        verify(userRepository, times(1)).save(appUser);
    }

    @Test
    void should_throw_exception_when_trying_to_rest_password() {
        when(userRepository.findByUserEmail(EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.resetPassword(EMAIL))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
        verify(emailSender, never()).sendEmail(anyString(), anyString());
        verify(userRepository, never()).save(any());
    }


}