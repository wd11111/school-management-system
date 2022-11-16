package pl.schoolmanagementsystem.common.teacher.exception;

import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.teacher.Teacher;

public class TeacherDoesNotTeachSubjectException extends RuntimeException {

    public TeacherDoesNotTeachSubjectException(Teacher teacher, SchoolSubject schoolSubject) {
        super(String.format("%s %s does not teach %s",
                teacher.getName(), teacher.getSurname(), schoolSubject.getName()));
    }
}
