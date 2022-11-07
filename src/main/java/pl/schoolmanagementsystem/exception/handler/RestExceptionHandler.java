package pl.schoolmanagementsystem.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.schoolmanagementsystem.exception.*;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(value = {
            ClassAlreadyExistsException.class,
            ClassAlreadyHasTeacherException.class,})
    public ResponseEntity<ErrorResponse> handleClassExceptions(ClassAlreadyExistsException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {
            TeacherDoesNotTeachClassException.class,
            TeacherAlreadyTeachesSubjectException.class})
    public ResponseEntity<ErrorResponse> handleTeacherExceptions(RuntimeException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {
            NoSuchSchoolClassException.class,
            NoSuchSchoolSubjectException.class,
            NoSuchStudentException.class,
            NoSuchTeacherException.class})
    public ResponseEntity<ErrorResponse> handleNoSuchExceptions(RuntimeException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }
}
