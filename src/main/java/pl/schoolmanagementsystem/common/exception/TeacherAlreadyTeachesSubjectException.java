package pl.schoolmanagementsystem.common.exception;

import pl.schoolmanagementsystem.common.model.SchoolSubject;
import pl.schoolmanagementsystem.common.model.Teacher;

public class TeacherAlreadyTeachesSubjectException extends RuntimeException {

    public TeacherAlreadyTeachesSubjectException(Teacher teacher, SchoolSubject schoolSubject) {
        super(String.format("%s %s already teaches %s",
                teacher.getName(), teacher.getSurname(), schoolSubject.getName()));
    }
}
