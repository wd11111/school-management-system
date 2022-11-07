package pl.schoolmanagementsystem.security.dto;

import lombok.Data;

@Data
public class LoginCredentials {

    private String email;
    private String password;
}
