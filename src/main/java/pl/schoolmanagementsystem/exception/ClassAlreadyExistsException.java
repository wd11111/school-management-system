package pl.schoolmanagementsystem.exception;

import pl.schoolmanagementsystem.model.dto.input.TextDto;

public class ClassAlreadyExistsException extends RuntimeException {
    public ClassAlreadyExistsException(TextDto schoolClassName) {
        super(String.format("Class %s already exists", schoolClassName.getPlainText()));
    }
}
