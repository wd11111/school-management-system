package pl.schoolmanagementsystem.exception;

public class NoSuchStudentException extends RuntimeException{

    public NoSuchStudentException(int studentId) {
        super(String.format("Student with such an id does not exist: %d", studentId));
    }
}
