package pl.schoolmanagementsystem.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.security.dto.LoginCredentials;
import pl.schoolmanagementsystem.security.service.UserService;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginCredentials loginCredentials) {
        userService.loadUserByUsername(loginCredentials.getEmail());
        return ResponseEntity.ok().build();
    }
}
