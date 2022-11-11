package pl.schoolmanagementsystem.teacher.exception;

import pl.schoolmanagementsystem.schoolsubject.SchoolSubject;
import pl.schoolmanagementsystem.teacher.Teacher;

public class TeacherAlreadyTeachesSubjectException extends RuntimeException {

    public TeacherAlreadyTeachesSubjectException(Teacher teacher, SchoolSubject schoolSubject) {
        super(String.format("%s %s already teaches %s",
                teacher.getName(), teacher.getSurname(), schoolSubject.getName()));
    }
}
