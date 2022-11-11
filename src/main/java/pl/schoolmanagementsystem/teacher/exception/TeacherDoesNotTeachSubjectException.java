package pl.schoolmanagementsystem.teacher.exception;

import pl.schoolmanagementsystem.schoolsubject.SchoolSubject;
import pl.schoolmanagementsystem.teacher.Teacher;

public class TeacherDoesNotTeachSubjectException extends RuntimeException {

    public TeacherDoesNotTeachSubjectException(Teacher teacher, SchoolSubject schoolSubject) {
        super(String.format("%s %s does not teach %s",
                teacher.getName(), teacher.getSurname(), schoolSubject.getName()));
    }
}
