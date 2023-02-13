package pl.schoolmanagementsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.schoolmanagementsystem.common.exception.*;
import pl.schoolmanagementsystem.exception.dto.ErrorResponse;
import pl.schoolmanagementsystem.security.exception.AuthenticationException;
import pl.schoolmanagementsystem.security.exception.CouldNotConfirmUserException;

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
    public ResponseEntity<ErrorResponse> handleForbiddenException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(exception.getMessage()));
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
            MarkNotInRangeException.class,
            FilterException.class,
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getMessage()));
    }

}
