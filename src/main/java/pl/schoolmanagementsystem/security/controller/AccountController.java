package pl.schoolmanagementsystem.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.security.dto.EmailDto;
import pl.schoolmanagementsystem.security.dto.PasswordDto;
import pl.schoolmanagementsystem.security.service.AccountService;

import javax.validation.Valid;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/confirm/{token}")
    public void confirm(@PathVariable String token, @RequestBody @Valid PasswordDto passwordDto) {
        accountService.confirmPassword(passwordDto, token);
    }

    @PostMapping("/reset-password")
    public void resetPassword(@RequestBody @Valid EmailDto email) {
        accountService.resetPassword(email.email());
    }
}
