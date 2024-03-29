package pl.schoolmanagementsystem.security.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.schoolmanagementsystem.common.model.AppUser;
import pl.schoolmanagementsystem.common.model.Role;
import pl.schoolmanagementsystem.common.repository.AppUserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String TOKEN = "token";
    public static final String ROLE = "ROLE";
    @Mock
    private AppUserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void should_correctly_load_user() {
        Role role = new Role();
        role.setRole(ROLE);
        AppUser appUser = new AppUser(1L, EMAIL, PASSWORD, TOKEN, List.of(role));
        when(userRepository.findByUserEmail(anyString())).thenReturn(Optional.of(appUser));

        UserDetails user = userService.loadUserByUsername(EMAIL);

        assertThat(user.getUsername()).isEqualTo(EMAIL);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);
        assertThat(user.getAuthorities()).extracting("role").isEqualTo(List.of(ROLE));
    }

    @Test
    void should_throw_exception_when_user_not_found() {
        when(userRepository.findByUserEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loadUserByUsername(EMAIL))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}