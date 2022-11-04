package pl.schoolmanagementsystem.exception;

public class NoSuchSchoolClassException extends RuntimeException {
    public NoSuchSchoolClassException(String schoolClassName) {
        super(String.format("Such a school class does not exist: %s", schoolClassName));
    }
}
