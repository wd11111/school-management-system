package pl.schoolmanagementsystem.schoolclass.exception;

public class NoSuchSchoolClassException extends RuntimeException {
    public NoSuchSchoolClassException(String incorrectSchoolClassName) {
        super(String.format("Such a school class does not exist: %s", incorrectSchoolClassName));
    }
}
