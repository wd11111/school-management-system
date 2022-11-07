package pl.schoolmanagementsystem.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super(String.format("User with this email doesnt exist"));
    }
}
