package pl.schoolmanagementsystem.common.exception;

import pl.schoolmanagementsystem.common.dto.SchoolClassDto;

public class ClassAlreadyExistsException extends RuntimeException {
    public ClassAlreadyExistsException(SchoolClassDto schoolClassDto) {
        super(String.format("Class %s already exists", schoolClassDto.getSchoolClassName()));
    }
}
