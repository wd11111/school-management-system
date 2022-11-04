package pl.schoolmanagementsystem.exception;

public class NoSuchSchoolSubjectException extends RuntimeException {

    public NoSuchSchoolSubjectException(String schoolSubjectName) {
        super(String.format("Such a school subject does not exist: %s", schoolSubjectName));
    }
}
