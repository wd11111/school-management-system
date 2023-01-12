package pl.schoolmanagementsystem;

import pl.schoolmanagementsystem.common.mark.Mark;
import pl.schoolmanagementsystem.common.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.common.mark.dto.MarkDto;
import pl.schoolmanagementsystem.common.schoolClass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.TaughtSubjectDto;
import pl.schoolmanagementsystem.common.student.dto.StudentDto;
import pl.schoolmanagementsystem.schoolClass.dto.AddTeacherToClassDto;
import pl.schoolmanagementsystem.schoolClass.dto.TeacherInClassDto;
import pl.schoolmanagementsystem.student.dto.CreateStudentDto;
import pl.schoolmanagementsystem.teacher.dto.CreateTeacherDto;
import pl.schoolmanagementsystem.teacher.dto.StudentWithMarksDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherDto;

import java.math.BigDecimal;
import java.util.*;

public interface ControllerSamples extends Samples {

    String CLASS_NAME = "1a";
    String NAME = "Adam";
    String SURNAME = "Nowak";
    String SUBJECT = "Biology";
    boolean IS_ADMIN = false;
    BigDecimal MARK = BigDecimal.valueOf(2.0);
    double AVERAGE_MARK = 3.0;

    default SchoolClassDto schoolClassDto() {
        return new SchoolClassDto(CLASS_NAME);
    }

    default StudentDto studentResponseDto2() {
        return new StudentDto(ID_1, NAME, SURNAME);
    }

    default TaughtSubjectDto subjectAndTeacherResponse() {
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

    default TeacherDto createTeacherDto() {
        return new TeacherDto(ID_1, NAME, SURNAME, Collections.emptySet());
    }

    default StudentWithMarksDto studentResponseDto3() {
        return new StudentWithMarksDto(ID_1, NAME, SURNAME, List.of(BigDecimal.ONE, BigDecimal.ONE));
    }

    default Mark getMark1() {
        return Mark.builder().mark(BigDecimal.ONE).build();
    }

    default Map<String, List<BigDecimal>> getGroupedMarksBySubject() {
        return new HashMap<>(Map.of(
                SUBJECT_BIOLOGY, List.of(MARK, MARK),
                SUBJECT_HISTORY, List.of(MARK, MARK)
        ));
    }

    default MarkDto markWithTwoFields1() {
        return new MarkDto(MARK, SUBJECT_BIOLOGY);
    }

    default MarkDto markWithTwoFields2() {
        return new MarkDto(MARK, SUBJECT_HISTORY);
    }

    default MarkAvgDto markAvgDto() {
        return new MarkAvgDto(SUBJECT_BIOLOGY, AVERAGE_MARK);
    }

}
