package pl.schoolmanagementsystem.exception;

import pl.schoolmanagementsystem.model.SchoolSubject;
import pl.schoolmanagementsystem.model.Teacher;

public class TeacherDoesNotTeachSubject extends RuntimeException {

    public TeacherDoesNotTeachSubject(Teacher teacher, SchoolSubject schoolSubject) {
        super(String.format("%s %s does not teach %s",
                teacher.getName(), teacher.getSurname(), schoolSubject.getSubjectName()));
    }
}
