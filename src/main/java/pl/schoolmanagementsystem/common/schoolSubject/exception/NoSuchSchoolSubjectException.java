package pl.schoolmanagementsystem.common.schoolSubject.exception;

public class NoSuchSchoolSubjectException extends RuntimeException {

    public NoSuchSchoolSubjectException(String incorrectSchoolSubjectName) {
        super(String.format("Such a school subject does not exist: %s", incorrectSchoolSubjectName));
    }
}
