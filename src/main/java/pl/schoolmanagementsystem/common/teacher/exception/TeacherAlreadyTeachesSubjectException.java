package pl.schoolmanagementsystem.common.teacher.exception;

import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.teacher.Teacher;

public class TeacherAlreadyTeachesSubjectException extends RuntimeException {

    public TeacherAlreadyTeachesSubjectException(Teacher teacher, SchoolSubject schoolSubject) {
        super(String.format("%s %s already teaches %s",
                teacher.getName(), teacher.getSurname(), schoolSubject.getName()));
    }
}
