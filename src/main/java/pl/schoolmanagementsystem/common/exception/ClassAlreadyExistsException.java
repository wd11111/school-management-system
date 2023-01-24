package pl.schoolmanagementsystem.common.exception;

public class ClassAlreadyExistsException extends RuntimeException {
    public ClassAlreadyExistsException(String schoolClassName) {
        super(String.format("Class %s already exists", schoolClassName));
    }
}
