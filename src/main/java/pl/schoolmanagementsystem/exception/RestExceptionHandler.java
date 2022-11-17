package pl.schoolmanagementsystem.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.schoolmanagementsystem.admin.schoolClass.exception.ClassAlreadyExistsException;
import pl.schoolmanagementsystem.admin.schoolClass.exception.ClassAlreadyHasTeacherException;
import pl.schoolmanagementsystem.admin.schoolClass.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.common.schoolSubject.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.common.schoolSubject.exception.SubjectAlreadyExistsException;
import pl.schoolmanagementsystem.common.security.CouldNotConfirmIUserException;
import pl.schoolmanagementsystem.common.security.PasswordsDoNotMatchException;
import pl.schoolmanagementsystem.common.student.exception.NoSuchStudentEmailException;
import pl.schoolmanagementsystem.common.student.exception.NoSuchStudentException;
import pl.schoolmanagementsystem.common.teacher.exception.NoSuchTeacherException;
import pl.schoolmanagementsystem.common.teacher.exception.TeacherAlreadyTeachesSubjectException;
import pl.schoolmanagementsystem.common.teacher.exception.TeacherDoesNotTeachClassException;
import pl.schoolmanagementsystem.common.teacher.exception.TeacherDoesNotTeachSubjectException;
import pl.schoolmanagementsystem.common.user.exception.EmailAlreadyInUseException;
import pl.schoolmanagementsystem.exception.dto.ErrorResponse;

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
            CouldNotConfirmIUserException.class,
            PasswordsDoNotMatchException.class})
    public ResponseEntity<ErrorResponse> handleSecurityConfirmationException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getMessage()));
    }
}
