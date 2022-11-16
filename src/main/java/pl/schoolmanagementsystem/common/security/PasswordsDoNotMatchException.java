package pl.schoolmanagementsystem.common.security;

public class PasswordsDoNotMatchException extends RuntimeException{

    public PasswordsDoNotMatchException() {
        super("Passwords do not match!");
    }
}
