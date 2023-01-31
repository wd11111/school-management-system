package pl.schoolmanagementsystem.security.exception;

public class AuthenticationException extends RuntimeException{

    public AuthenticationException() {
        super("Authentication failed");
    }
}
