package pl.schoolmanagementsystem.common.security;

public class CouldNotConfirmUserException extends RuntimeException{

    public CouldNotConfirmUserException() {
        super("Could not confirm user!");
    }
}
