package pl.schoolmanagementsystem.common.exception;

public class NoSuchTeacherException extends RuntimeException {

    public NoSuchTeacherException(long id) {
        super(String.format("Teacher with such an id does not exist: %d", id));
    }
}