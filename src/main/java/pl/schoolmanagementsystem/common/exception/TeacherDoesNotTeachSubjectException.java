package pl.schoolmanagementsystem.common.exception;

import pl.schoolmanagementsystem.common.model.SchoolSubject;
import pl.schoolmanagementsystem.common.model.Teacher;

public class TeacherDoesNotTeachSubjectException extends RuntimeException {

    public TeacherDoesNotTeachSubjectException(Teacher teacher, SchoolSubject schoolSubject) {
        super(String.format("%s %s does not teach %s",
                teacher.getName(), teacher.getSurname(), schoolSubject.getName()));
    }
}
