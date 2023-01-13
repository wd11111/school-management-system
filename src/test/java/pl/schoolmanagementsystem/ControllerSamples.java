package pl.schoolmanagementsystem;

import pl.schoolmanagementsystem.common.dto.SchoolClassDto;
import pl.schoolmanagementsystem.common.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.dto.StudentDto;
import pl.schoolmanagementsystem.common.dto.TaughtSubjectDto;
import pl.schoolmanagementsystem.schoolClass.dto.AddTeacherToClassDto;
import pl.schoolmanagementsystem.schoolClass.dto.TeacherInClassDto;
import pl.schoolmanagementsystem.student.dto.CreateStudentDto;
import pl.schoolmanagementsystem.teacher.dto.CreateTeacherDto;
import pl.schoolmanagementsystem.teacher.dto.StudentWithMarksDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherDto;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public interface ControllerSamples extends Samples {

    String CLASS_NAME = "1a";
    String NAME = "Adam";
    String SURNAME = "Nowak";
    String SUBJECT = "Biology";
    boolean IS_ADMIN = false;

    default SchoolClassDto schoolClassDto() {
        return new SchoolClassDto(CLASS_NAME);
    }

    default StudentDto studentResponseDto2() {
        return new StudentDto(ID_1, NAME, SURNAME);
    }

    default TaughtSubjectDto createTaughtSubjectDto() {
        return new TaughtSubjectDto(SUBJECT, NAME, SURNAME);
    }

    default AddTeacherToClassDto teacherInClassRequest() {
        return new AddTeacherToClassDto(ID_1, SUBJECT);
    }

    default TeacherInClassDto teacherInClassResponse() {
        return new TeacherInClassDto(ID_1, SUBJECT, Set.of(CLASS_NAME));
    }

    default SchoolSubjectDto schoolSubjectDto() {
        return new SchoolSubjectDto(CLASS_NAME);
    }

    default CreateStudentDto studentRequestDto() {
        return new CreateStudentDto(NAME, SURNAME, CLASS_NAME, NAME);
    }

    default StudentWithMarksDto studentWithMarksDto() {
        return new StudentWithMarksDto(ID_1, NAME, SURNAME, List.of(BigDecimal.ONE, BigDecimal.ONE));
    }

    default TeacherDto teacherResponseDto() {
        return new TeacherDto(ID_2, NAME3, SURNAME2, Set.of(SUBJECT_BIOLOGY));
    }

    default CreateTeacherDto createCreateTeacherDto() {
        return new CreateTeacherDto(NAME, NAME, SURNAME, IS_ADMIN, Collections.emptySet());
    }

}
