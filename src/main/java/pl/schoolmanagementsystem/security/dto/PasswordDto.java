package pl.schoolmanagementsystem.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDto {

    private String password;
    private String confirmPassword;
}