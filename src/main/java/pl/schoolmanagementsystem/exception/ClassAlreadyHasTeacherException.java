package pl.schoolmanagementsystem.exception;

import pl.schoolmanagementsystem.schoolclass.model.SchoolClass;
import pl.schoolmanagementsystem.schoolsubject.model.SchoolSubject;
import pl.schoolmanagementsystem.teacherinclass.model.TeacherInClass;

public class ClassAlreadyHasTeacherException extends RuntimeException {

    public ClassAlreadyHasTeacherException(TeacherInClass teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        super(String.format("%s %s already teaches %s in %s",
                teacher.getTeacher().getName(), teacher.getTeacher().getSurname(),
                schoolSubject.getName(), schoolClass.getName()));
    }
}
