package pl.schoolmanagementsystem.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.security.dto.PasswordDto;
import pl.schoolmanagementsystem.security.service.ConfirmAccountService;

@RestController
@RequestMapping("/confirm")
@RequiredArgsConstructor
public class ConfirmAccountController {

    private final ConfirmAccountService confirmAccountService;

    @PostMapping("/{token}")
    public void confirm(@PathVariable String token, @RequestBody PasswordDto passwordDto) {
        confirmAccountService.confirm(passwordDto, token);
    }
}
