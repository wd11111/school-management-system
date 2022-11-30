package pl.schoolmanagementsystem.common.student.exception;

public class NoSuchStudentException extends RuntimeException{

    public NoSuchStudentException(long studentId) {
        super(String.format("Student with such an id does not exist: %d", studentId));
    }

    public NoSuchStudentException(String studentEmail) {
        super(String.format("Student with such an email does not exist: %s", studentEmail));
    }
}
