package pl.schoolmanagementsystem.security.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.security.dto.LoginCredentials;

import javax.validation.Valid;

@RestController
public class LoginController {

    @PostMapping("/login")
    public void login(@RequestBody @Valid LoginCredentials loginCredentials) {
    }

}
