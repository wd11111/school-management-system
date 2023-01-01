package pl.schoolmanagementsystem.admin.security;

public class PasswordsDoNotMatchException extends RuntimeException{

    public PasswordsDoNotMatchException() {
        super("Passwords do not match!");
    }
}
