package pl.schoolmanagementsystem.common.role;

import pl.schoolmanagementsystem.common.model.AppUser;

import java.util.ArrayList;

import static pl.schoolmanagementsystem.common.email.token.TokenGenerator.generateToken;

public class AppUserService {

    public static final String PASSWORD = null;

    public static AppUser createAppUser(String email) {
        return new AppUser(email, PASSWORD, generateToken(), new ArrayList<>());
    }
}
