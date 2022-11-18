package pl.schoolmanagementsystem;

import pl.schoolmanagementsystem.common.schoolClass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.common.schoolClass.dto.TeacherInClassInputDto;
import pl.schoolmanagementsystem.common.schoolClass.dto.TeacherInClassOutputDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndTeacherOutputDto;
import pl.schoolmanagementsystem.common.student.dto.StudentOutputDto2;
import pl.schoolmanagementsystem.common.teacher.TeacherInClass;

import java.util.Set;

public interface ControllerSamples extends Samples{

    String CLASS_NAME = "1a";
    int ID = 1;
    String NAME = "Adam";
    String SURNAME = "Nowak";
    String SUBJECT = "Biology";

    default SchoolClassDto schoolClassDto() {
        return new SchoolClassDto(CLASS_NAME);
    }

    default StudentOutputDto2 studentOutputDto2() {
        return new StudentOutputDto2(ID, NAME, SURNAME);
    }

    default SubjectAndTeacherOutputDto subjectAndTeacherOutput() {
        return new SubjectAndTeacherOutputDto(SUBJECT, NAME, SURNAME);
    }

    default TeacherInClassInputDto teacherInClassInput() {
        return new TeacherInClassInputDto(ID, SUBJECT);
    }
    default TeacherInClassOutputDto teacherInClassOutput() {
        return new TeacherInClassOutputDto(ID, SUBJECT, Set.of(CLASS_NAME));
    }
    default TeacherInClass teacherInClass() {
        return new TeacherInClass(1, createTeacherNoSubjectsTaught(), SUBJECT, Set.of(createSchoolClass()));
    }

    default SchoolSubjectDto schoolSubjectDto() {
        return new SchoolSubjectDto(CLASS_NAME);
    }
}
