package pl.schoolmanagementsystem.exception;

import pl.schoolmanagementsystem.model.dto.SchoolClassDto;

public class ClassAlreadyExistsException extends RuntimeException {
    public ClassAlreadyExistsException(SchoolClassDto schoolClassDto) {
        super(String.format("Class %s already exists", schoolClassDto.getSchoolClassName()));
    }
}
