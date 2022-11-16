package pl.schoolmanagementsystem.common.schoolSubject.exception;

import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;

public class SubjectAlreadyExistsException extends RuntimeException {

    public SubjectAlreadyExistsException(SchoolSubjectDto schoolSubjectDto) {
        super(String.format("Subject %s already exists", schoolSubjectDto.getSubjectName()));
    }
}
