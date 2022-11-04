package pl.schoolmanagementsystem.exception;

import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.model.SchoolSubject;

public class TeacherDoesNotTeachThisClassException extends RuntimeException {

    public TeacherDoesNotTeachThisClassException(SchoolSubject schoolSubject, SchoolClass schoolClass) {
        super(String.format("You do not teach %s in %s",
                schoolSubject.getSubjectName(), schoolClass.getSchoolClassName()));
    }
}
