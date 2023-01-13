package pl.schoolmanagementsystem.common.exception;

import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.common.model.SchoolSubject;
import pl.schoolmanagementsystem.common.model.TeacherInClass;

public class ClassAlreadyHasTeacherException extends RuntimeException {

    public ClassAlreadyHasTeacherException(TeacherInClass teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        super(String.format("%s %s already teaches %s in %s",
                teacher.getTeacher().getName(),
                teacher.getTeacher().getSurname(),
                schoolSubject.getName(),
                schoolClass.getName()));
    }
}
