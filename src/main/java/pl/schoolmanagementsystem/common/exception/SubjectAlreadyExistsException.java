package pl.schoolmanagementsystem.common.exception;

import pl.schoolmanagementsystem.common.dto.SchoolSubjectDto;

public class SubjectAlreadyExistsException extends RuntimeException {

    public SubjectAlreadyExistsException(SchoolSubjectDto schoolSubjectDto) {
        super(String.format("Subject %s already exists", schoolSubjectDto.getSubjectName()));
    }
}
