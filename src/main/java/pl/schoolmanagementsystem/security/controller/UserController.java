package pl.schoolmanagementsystem.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.security.dto.LoginCredentials;
import pl.schoolmanagementsystem.security.service.UserService;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginCredentials loginCredentials) {
        userService.loadUserByUsername(loginCredentials.getEmail());
        return ResponseEntity.ok().build();
    }
}
