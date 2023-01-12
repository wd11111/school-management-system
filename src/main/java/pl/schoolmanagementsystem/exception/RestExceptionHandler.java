package pl.schoolmanagementsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.schoolmanagementsystem.common.mark.exception.MarkNotInRangeException;
import pl.schoolmanagementsystem.common.schoolClass.exception.ClassAlreadyExistsException;
import pl.schoolmanagementsystem.common.schoolClass.exception.ClassAlreadyHasTeacherException;
import pl.schoolmanagementsystem.common.schoolClass.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.common.schoolSubject.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.common.schoolSubject.exception.SubjectAlreadyExistsException;
import pl.schoolmanagementsystem.common.student.exception.NoSuchStudentEmailException;
import pl.schoolmanagementsystem.common.student.exception.NoSuchStudentException;
import pl.schoolmanagementsystem.common.teacher.exception.NoSuchTeacherException;
import pl.schoolmanagementsystem.common.teacher.exception.TeacherAlreadyTeachesSubjectException;
import pl.schoolmanagementsystem.common.teacher.exception.TeacherDoesNotTeachClassException;
import pl.schoolmanagementsystem.common.teacher.exception.TeacherDoesNotTeachSubjectException;
import pl.schoolmanagementsystem.common.user.exception.EmailAlreadyInUseException;
import pl.schoolmanagementsystem.exception.dto.ErrorResponse;
import pl.schoolmanagementsystem.security.exception.CouldNotConfirmUserException;
import pl.schoolmanagementsystem.security.exception.PasswordsDoNotMatchException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {
            ClassAlreadyExistsException.class,
            ClassAlreadyHasTeacherException.class,
            EmailAlreadyInUseException.class,
            SubjectAlreadyExistsException.class,
            TeacherAlreadyTeachesSubjectException.class,
            TeacherDoesNotTeachSubjectException.class,
    })
    public ResponseEntity<ErrorResponse> handleConflictException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(TeacherDoesNotTeachClassException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(TeacherDoesNotTeachClassException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(value = {
            NoSuchSchoolClassException.class,
            NoSuchSchoolSubjectException.class,
            NoSuchStudentException.class,
            NoSuchTeacherException.class,
            NoSuchStudentEmailException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(value = {
            CouldNotConfirmUserException.class,
            PasswordsDoNotMatchException.class,
            MarkNotInRangeException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getMessage()));
    }
}
