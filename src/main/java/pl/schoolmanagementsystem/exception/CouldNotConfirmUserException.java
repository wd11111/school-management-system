package pl.schoolmanagementsystem.exception;

public class CouldNotConfirmUserException extends RuntimeException{

    public CouldNotConfirmUserException() {
        super("Could not confirm user!");
    }
}
