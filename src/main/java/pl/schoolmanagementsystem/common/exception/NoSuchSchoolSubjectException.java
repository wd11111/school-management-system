package pl.schoolmanagementsystem.common.exception;

public class NoSuchSchoolSubjectException extends RuntimeException {

    public NoSuchSchoolSubjectException() {
        super("Some of given subjects does not exist");
    }

    public NoSuchSchoolSubjectException(String incorrectSchoolSubjectName) {
        super(String.format("Such a school subject does not exist: %s", incorrectSchoolSubjectName));
    }
}
