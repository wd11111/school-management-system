package pl.schoolmanagementsystem.common.security;

public class CouldNotConfirmIUserException extends RuntimeException{

    public CouldNotConfirmIUserException() {
        super("Could not confirm user!");
    }
}
