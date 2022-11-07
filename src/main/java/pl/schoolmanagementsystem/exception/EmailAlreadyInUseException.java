package pl.schoolmanagementsystem.exception;

public class EmailAlreadyInUseException extends RuntimeException{

    public EmailAlreadyInUseException(String emailInUse) {
        super(String.format("K", emailInUse));
    }
}
