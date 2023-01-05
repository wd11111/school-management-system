package pl.schoolmanagementsystem.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.schoolmanagementsystem.exception.CouldNotConfirmUserException;
import pl.schoolmanagementsystem.exception.PasswordsDoNotMatchException;
import pl.schoolmanagementsystem.exception.*;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(value = {
            ClassAlreadyExistsException.class,
            ClassAlreadyHasTeacherException.class,})
    public ResponseEntity<ErrorResponse> handleClassExceptions(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(value = EmailAlreadyInUseException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyInUse(EmailAlreadyInUseException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(value = SubjectAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleSubjectExceptions(SubjectAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(value = {
            TeacherAlreadyTeachesSubjectException.class,
            TeacherDoesNotTeachSubjectException.class})
    public ResponseEntity<ErrorResponse> handleTeacherExceptions(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(TeacherDoesNotTeachClassException.class)
    public ResponseEntity<ErrorResponse> handleTeacherDoesNotTeachClassException(TeacherDoesNotTeachClassException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(exception.getMessage()));

    }

    @ExceptionHandler(value = {
            NoSuchSchoolClassException.class,
            NoSuchSchoolSubjectException.class,
            NoSuchStudentException.class,
            NoSuchTeacherException.class,
            NoSuchStudentEmailException.class})
    public ResponseEntity<ErrorResponse> handleNoSuchExceptions(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(value = {
            CouldNotConfirmUserException.class,
            PasswordsDoNotMatchException.class})
    public ResponseEntity<ErrorResponse> handleSecurityConfirmationException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }
}
