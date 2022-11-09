package pl.schoolmanagementsystem.exception;

import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.model.SchoolSubject;

public class TeacherDoesNotTeachClassException extends RuntimeException {

    public TeacherDoesNotTeachClassException(SchoolSubject schoolSubject, SchoolClass schoolClass) {
        super(String.format("You do not teach %s in %s",
                schoolSubject.getName(), schoolClass.getName()));
    }
}
