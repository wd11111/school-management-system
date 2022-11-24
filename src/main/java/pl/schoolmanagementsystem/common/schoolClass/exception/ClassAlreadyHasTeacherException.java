package pl.schoolmanagementsystem.common.schoolClass.exception;

import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.teacher.TeacherInClass;

public class ClassAlreadyHasTeacherException extends RuntimeException {

    public ClassAlreadyHasTeacherException(TeacherInClass teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        super(String.format("%s %s already teaches %s in %s",
                teacher.getTeacher().getName(), teacher.getTeacher().getSurname(),
                schoolSubject.getName(), schoolClass.getName()));
    }
}
