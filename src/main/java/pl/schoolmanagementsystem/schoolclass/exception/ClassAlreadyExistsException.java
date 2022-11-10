package pl.schoolmanagementsystem.schoolclass.exception;

import pl.schoolmanagementsystem.schoolclass.dto.SchoolClassDto;

public class ClassAlreadyExistsException extends RuntimeException {
    public ClassAlreadyExistsException(SchoolClassDto schoolClassDto) {
        super(String.format("Class %s already exists", schoolClassDto.getSchoolClassName()));
    }
}
