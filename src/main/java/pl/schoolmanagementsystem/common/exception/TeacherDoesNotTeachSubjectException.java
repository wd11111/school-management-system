package pl.schoolmanagementsystem.common.exception;

import pl.schoolmanagementsystem.common.model.Teacher;

public class TeacherDoesNotTeachSubjectException extends RuntimeException {

    public TeacherDoesNotTeachSubjectException(Teacher teacher, String schoolSubject) {
        super(String.format("%s %s does not teach %s",
                teacher.getName(), teacher.getSurname(), schoolSubject));
    }

    public TeacherDoesNotTeachSubjectException(String schoolSubject) {
        super(String.format("You do not teach %s", schoolSubject));
    }
}
