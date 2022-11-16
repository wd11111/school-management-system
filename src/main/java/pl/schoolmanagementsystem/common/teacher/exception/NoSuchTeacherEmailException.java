package pl.schoolmanagementsystem.common.teacher.exception;

public class NoSuchTeacherEmailException extends RuntimeException{

    public NoSuchTeacherEmailException(String email) {
        super(String.format("Teacher with such an email does not exist: %s", email));
    }
}
