package pl.schoolmanagementsystem.schoolclass.exception;

import pl.schoolmanagementsystem.schoolclass.SchoolClass;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubject;
import pl.schoolmanagementsystem.teacherinclass.TeacherInClass;

public class ClassAlreadyHasTeacherException extends RuntimeException {

    public ClassAlreadyHasTeacherException(TeacherInClass teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        super(String.format("%s %s already teaches %s in %s",
                teacher.getTeacher().getName(), teacher.getTeacher().getSurname(),
                schoolSubject.getName(), schoolClass.getName()));
    }
}
