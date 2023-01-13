package pl.schoolmanagementsystem.common.exception;

public class NoSuchStudentException extends RuntimeException{

    public NoSuchStudentException(long studentId) {
        super(String.format("Student with such an id does not exist: %d", studentId));
    }
}
