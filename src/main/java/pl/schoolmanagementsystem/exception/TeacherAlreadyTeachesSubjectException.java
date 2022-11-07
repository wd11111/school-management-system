package pl.schoolmanagementsystem.exception;

import pl.schoolmanagementsystem.model.SchoolSubject;
import pl.schoolmanagementsystem.model.Teacher;

public class TeacherAlreadyTeachesSubjectException extends RuntimeException {

    public TeacherAlreadyTeachesSubjectException(Teacher teacher, SchoolSubject schoolSubject) {
        super(String.format("%s %s already teaches %s",
                teacher.getName(), teacher.getSurname(), schoolSubject.getSubjectName()));
    }
}
