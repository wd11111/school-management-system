package pl.schoolmanagementsystem.mark.uitls;

import pl.schoolmanagementsystem.mark.Mark;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubject;
import pl.schoolmanagementsystem.student.Student;

public class MarkBuilder {

    public static Mark build(int mark, Student student, SchoolSubject schoolSubject) {
        return Mark.builder()
                .mark(mark)
                .student(student)
                .subject(schoolSubject)
                .build();
    }
}
