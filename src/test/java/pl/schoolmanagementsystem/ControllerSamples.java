package pl.schoolmanagementsystem;

import pl.schoolmanagementsystem.common.mark.Mark;
import pl.schoolmanagementsystem.common.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.common.mark.dto.MarkDto;
import pl.schoolmanagementsystem.common.schoolClass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.admin.schoolClass.dto.TeacherInClassRequestDto;
import pl.schoolmanagementsystem.admin.schoolClass.dto.TeacherInClassResponseDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndTeacherResponseDto;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.admin.student.dto.StudentRequestDto;
import pl.schoolmanagementsystem.common.student.dto.StudentResponseDto2;
import pl.schoolmanagementsystem.teacher.dto.StudentResponseDto3;
import pl.schoolmanagementsystem.common.teacher.TeacherInClass;
import pl.schoolmanagementsystem.admin.teacher.dto.TeacherRequestDto;
import pl.schoolmanagementsystem.admin.teacher.dto.TeacherResponseDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ControllerSamples extends Samples {

    String CLASS_NAME = "1a";
    String NAME = "Adam";
    String SURNAME = "Nowak";
    String SUBJECT = "Biology";
    boolean IS_ADMIN = false;
    byte OUT_OF_RANGE_MARK = 8;
    byte MARK = 2;
    double AVERAGE_MARK = 3.0;

    default SchoolClassDto schoolClassDto() {
        return new SchoolClassDto(CLASS_NAME);
    }

    default StudentResponseDto2 studentResponseDto2() {
        return new StudentResponseDto2(ID_1, NAME, SURNAME);
    }

    default SubjectAndTeacherResponseDto subjectAndTeacherResponse() {
        return new SubjectAndTeacherResponseDto(SUBJECT, NAME, SURNAME);
    }

    default TeacherInClassRequestDto teacherInClassRequest() {
        return new TeacherInClassRequestDto(ID_1, SUBJECT);
    }

    default TeacherInClassResponseDto teacherInClassResponse() {
        return new TeacherInClassResponseDto(ID_1, SUBJECT, Set.of(CLASS_NAME));
    }

    default TeacherInClass teacherInClass() {
        return new TeacherInClass(1, createTeacherNoSubjectsTaught(), SUBJECT, Set.of(createSchoolClass()));
    }

    default SchoolSubjectDto schoolSubjectDto() {
        return new SchoolSubjectDto(CLASS_NAME);
    }

    default StudentRequestDto studentRequestDto() {
        return new StudentRequestDto(NAME, SURNAME, CLASS_NAME, NAME);
    }

    default Student student() {
        return new Student(ID_1, NAME, SURNAME, getAppUser(), List.of(getMark1(), getMark1(), getMark1()), CLASS_NAME);
    }

    default TeacherResponseDto teacherResponseDto() {
        return new TeacherResponseDto(ID_2, NAME3, SURNAME2, Set.of(SUBJECT_BIOLOGY));
    }

    default TeacherRequestDto teacherRequestDto() {
        return new TeacherRequestDto(NAME, NAME, SURNAME, IS_ADMIN, Collections.emptySet());
    }

    default StudentResponseDto3 studentResponseDto3() {
        return new StudentResponseDto3(ID_1, NAME, SURNAME, List.of((byte) 1, (byte) 1, (byte) 1));
    }

    default Mark getMark1() {
        return Mark.builder().mark((byte) 1).build();
    }

    default Map<String, List<MarkDto>> getGroupedMarksBySubject() {
        return new HashMap<>(Map.of(
                SUBJECT_BIOLOGY, List.of(markWithTwoFields1(), markWithTwoFields1()),
                SUBJECT_HISTORY, List.of(markWithTwoFields2(), markWithTwoFields2())
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
