package pl.schoolmanagementsystem.mark.uitls;

import pl.schoolmanagementsystem.mark.model.Mark;
import pl.schoolmanagementsystem.schoolsubject.model.SchoolSubject;
import pl.schoolmanagementsystem.student.model.Student;

public class MarkBuilder {

    public static Mark build(int mark, Student student, SchoolSubject schoolSubject) {
        return Mark.builder()
                .mark(mark)
                .student(student)
                .subject(schoolSubject)
                .build();
    }
}
