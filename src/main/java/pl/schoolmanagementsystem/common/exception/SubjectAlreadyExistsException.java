package pl.schoolmanagementsystem.common.exception;

public class SubjectAlreadyExistsException extends RuntimeException {

    public SubjectAlreadyExistsException(String schoolSubjectName) {
        super(String.format("Subject %s already exists", schoolSubjectName));
    }
}
