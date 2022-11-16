package pl.schoolmanagementsystem.common.teacher.exception;

public class NoSuchTeacherException extends RuntimeException {

    public NoSuchTeacherException(String email) {
        super(String.format("Teacher with such an email does not exist: %s", email));
    }

    public NoSuchTeacherException(int id) {
        super(String.format("Teacher with such an id does not exist: %d", id));
    }
}