package pl.schoolmanagementsystem.common.schoolClass.exception;

import pl.schoolmanagementsystem.common.schoolClass.dto.SchoolClassDto;

public class ClassAlreadyExistsException extends RuntimeException {
    public ClassAlreadyExistsException(SchoolClassDto schoolClassDto) {
        super(String.format("Class %s already exists", schoolClassDto.getSchoolClassName()));
    }
}
