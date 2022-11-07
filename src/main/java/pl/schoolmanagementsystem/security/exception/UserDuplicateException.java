package pl.schoolmanagementsystem.security.exception;

public class UserDuplicateException extends RuntimeException{

    public UserDuplicateException(String userName) {
        super(String.format("User \"%s\" already exists!", userName));
    }
}
