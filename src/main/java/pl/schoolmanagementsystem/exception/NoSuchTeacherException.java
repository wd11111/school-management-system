package pl.schoolmanagementsystem.exception;

public class NoSuchTeacherException extends RuntimeException {

    public NoSuchTeacherException(String email) {
        super(String.format("Teacher with such an email does not exist: %s", email));
    }

    public NoSuchTeacherException(long id) {
        super(String.format("Teacher with such an id does not exist: %d", id));
    }
}