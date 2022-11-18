package pl.schoolmanagementsystem;

import pl.schoolmanagementsystem.common.schoolClass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndTeacherOutputDto;
import pl.schoolmanagementsystem.common.student.dto.StudentOutputDto2;

public interface ControllerSamples {

    String CLASS_NAME = "1a";
    int STUDENT_ID = 1;
    String NAME = "Adam";
    String SURNAME = "Nowak";
    String SUBJECT = "Biology";

    default SchoolClassDto schoolClassDto() {
        return new SchoolClassDto(CLASS_NAME);
    }

    default StudentOutputDto2 studentOutputDto2() {
        return new StudentOutputDto2(STUDENT_ID, NAME, SURNAME);
    }

    default SubjectAndTeacherOutputDto subjectAndTeacherOutput() {
        return new SubjectAndTeacherOutputDto(SUBJECT, NAME, SURNAME);
    }
}
