package pl.schoolmanagementsystem.exception;

import pl.schoolmanagementsystem.model.dto.SchoolSubjectDto;

public class SubjectAlreadyExistsException extends RuntimeException {

    public SubjectAlreadyExistsException(SchoolSubjectDto schoolSubjectDto) {
        super(String.format("Subject %s already exists", schoolSubjectDto.getSubject()));
    }
}
