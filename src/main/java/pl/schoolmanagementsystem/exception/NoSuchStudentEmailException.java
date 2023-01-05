package pl.schoolmanagementsystem.exception;

public class NoSuchStudentEmailException extends RuntimeException{

    public NoSuchStudentEmailException(String email) {
        super(String.format("Student with such an email does not exist: %s", email));
    }
}
