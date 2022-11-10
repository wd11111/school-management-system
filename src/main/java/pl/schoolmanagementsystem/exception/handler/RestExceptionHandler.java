package pl.schoolmanagementsystem.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.schoolmanagementsystem.email.exception.EmailAlreadyInUseException;
import pl.schoolmanagementsystem.exception.dto.ErrorResponse;
import pl.schoolmanagementsystem.schoolclass.exception.ClassAlreadyExistsException;
import pl.schoolmanagementsystem.schoolclass.exception.ClassAlreadyHasTeacherException;
import pl.schoolmanagementsystem.schoolclass.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.schoolsubject.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.schoolsubject.exception.SubjectAlreadyExistsException;
import pl.schoolmanagementsystem.student.exception.NoSuchStudentEmailException;
import pl.schoolmanagementsystem.student.exception.NoSuchStudentException;
import pl.schoolmanagementsystem.teacher.exception.NoSuchTeacherException;
import pl.schoolmanagementsystem.teacher.exception.TeacherAlreadyTeachesSubjectException;
import pl.schoolmanagementsystem.teacher.exception.TeacherDoesNotTeachClassException;
import pl.schoolmanagementsystem.teacher.exception.TeacherDoesNotTeachSubjectException;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(value = {
            ClassAlreadyExistsException.class,
            ClassAlreadyHasTeacherException.class,})
    public ResponseEntity<ErrorResponse> handleClassExceptions(RuntimeException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = EmailAlreadyInUseException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyInUse(EmailAlreadyInUseException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = SubjectAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleSubjectExceptions(SubjectAlreadyExistsException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {
            TeacherDoesNotTeachClassException.class,
            TeacherAlreadyTeachesSubjectException.class,
            TeacherDoesNotTeachSubjectException.class})
    public ResponseEntity<ErrorResponse> handleTeacherExceptions(RuntimeException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {
            NoSuchSchoolClassException.class,
            NoSuchSchoolSubjectException.class,
            NoSuchStudentException.class,
            NoSuchTeacherException.class,
            NoSuchStudentEmailException.class})
    public ResponseEntity<ErrorResponse> handleNoSuchExceptions(RuntimeException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }
}
