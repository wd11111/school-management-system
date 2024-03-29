package pl.schoolmanagementsystem.common.exception;

public class EmailAlreadyInUseException extends RuntimeException{

    public EmailAlreadyInUseException(String emailInUse) {
        super(String.format("Email already in use: %s", emailInUse));
    }
}
