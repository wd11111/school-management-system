package pl.schoolmanagementsystem.teacher.exception;

public class NoSuchTeacherException extends RuntimeException {

    public NoSuchTeacherException(int teacherId) {
        super(String.format("Teacher with such an id does not exist: %d", teacherId));
    }
}