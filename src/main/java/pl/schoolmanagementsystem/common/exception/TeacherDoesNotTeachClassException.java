package pl.schoolmanagementsystem.common.exception;

public class TeacherDoesNotTeachClassException extends RuntimeException {

    public TeacherDoesNotTeachClassException(String schoolSubject, String schoolClass) {
        super(String.format("You do not teach %s in %s",
                schoolSubject, schoolClass));
    }
}
