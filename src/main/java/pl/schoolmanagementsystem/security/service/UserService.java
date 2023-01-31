package pl.schoolmanagementsystem.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.common.model.AppUser;
import pl.schoolmanagementsystem.common.repository.AppUserRepository;
import pl.schoolmanagementsystem.security.exception.AuthenticationException;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUserEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));

        validatePassword(appUser.getPassword());

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        appUser.getRoles()
                .forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRole())));

        return new User(appUser.getUserEmail(), appUser.getPassword(), authorities);
    }

    private void validatePassword(String password) {
        if (password == null) {
            throw new AuthenticationException();
        }
    }
}
