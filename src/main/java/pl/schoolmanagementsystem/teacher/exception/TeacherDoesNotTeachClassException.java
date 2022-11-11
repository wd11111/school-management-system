package pl.schoolmanagementsystem.teacher.exception;

import pl.schoolmanagementsystem.schoolclass.SchoolClass;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubject;

public class TeacherDoesNotTeachClassException extends RuntimeException {

    public TeacherDoesNotTeachClassException(SchoolSubject schoolSubject, SchoolClass schoolClass) {
        super(String.format("You do not teach %s in %s",
                schoolSubject.getName(), schoolClass.getName()));
    }
}
