package pl.schoolmanagementsystem.common.exception;

public class TeacherDoesNotTeachClassException extends RuntimeException {

    public TeacherDoesNotTeachClassException(String schoolSubject, String schoolClass) {
        super(String.format("You do not teach %s in %s",
                schoolSubject, schoolClass));
    }

    public TeacherDoesNotTeachClassException(Long id, String schoolSubject, String schoolClass) {
        super(String.format("Teacher with id %d does not teach %s in %s",
                id, schoolSubject, schoolClass));
    }
}
