package pl.schoolmanagementsystem.exception;

import pl.schoolmanagementsystem.schoolclass.model.SchoolClass;
import pl.schoolmanagementsystem.schoolsubject.model.SchoolSubject;

public class TeacherDoesNotTeachClassException extends RuntimeException {

    public TeacherDoesNotTeachClassException(SchoolSubject schoolSubject, SchoolClass schoolClass) {
        super(String.format("You do not teach %s in %s",
                schoolSubject.getName(), schoolClass.getName()));
    }
}
