package pl.schoolmanagementsystem;

import pl.schoolmanagementsystem.common.mark.Mark;
import pl.schoolmanagementsystem.common.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.common.mark.dto.MarkWithTwoFields;
import pl.schoolmanagementsystem.common.schoolClass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.common.schoolClass.dto.TeacherInClassInputDto;
import pl.schoolmanagementsystem.common.schoolClass.dto.TeacherInClassOutputDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndTeacherOutputDto;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.common.student.dto.StudentInputDto;
import pl.schoolmanagementsystem.common.student.dto.StudentOutputDto2;
import pl.schoolmanagementsystem.common.student.dto.StudentOutputDto3;
import pl.schoolmanagementsystem.common.teacher.TeacherInClass;
import pl.schoolmanagementsystem.common.teacher.dto.TeacherInputDto;
import pl.schoolmanagementsystem.common.teacher.dto.TeacherOutputDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ControllerSamples extends Samples {

    String CLASS_NAME = "1a";
    int ID = 1;
    String NAME = "Adam";
    String SURNAME = "Nowak";
    String SUBJECT = "Biology";
    boolean IS_ADMIN = false;
    int OUT_OF_RANGE_MARK = 8;
    int MARK = 2;
    double AVERAGE_MARK = 3.0;

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

    default StudentInputDto studentInputDto() {
        return new StudentInputDto(NAME, SURNAME, CLASS_NAME, NAME);
    }

    default Student student() {
        return new Student(ID, NAME, SURNAME, getAppUser(), List.of(getMark1(), getMark1(), getMark1()), CLASS_NAME);
    }

    default TeacherOutputDto teacherOutputDto() {
        return new TeacherOutputDto(ID_2, NAME3, SURNAME2, Set.of(SUBJECT_BIOLOGY));
    }

    default TeacherInputDto teacherInputDto() {
        return new TeacherInputDto(NAME, NAME, SURNAME, IS_ADMIN, Collections.emptySet());
    }

    default StudentOutputDto3 studentOutputDto3() {
        return new StudentOutputDto3(ID, NAME, SURNAME, List.of(1, 1, 1));
    }

    default Mark getMark1() {
        return Mark.builder().mark(1).build();
    }

    default Map<String, List<MarkWithTwoFields>> getGroupedMarksBySubject() {
        return new HashMap<>(Map.of(
                SUBJECT_BIOLOGY, List.of(markWithTwoFields1(), markWithTwoFields1()),
                SUBJECT_HISTORY, List.of(markWithTwoFields2(), markWithTwoFields2())
        ));
    }

    default MarkWithTwoFields markWithTwoFields1() {
        return new MarkWithTwoFields(MARK, SUBJECT_BIOLOGY);
    }

    default MarkWithTwoFields markWithTwoFields2() {
        return new MarkWithTwoFields(MARK, SUBJECT_HISTORY);
    }

    default MarkAvgDto markAvgDto() {
        return new MarkAvgDto(SUBJECT_BIOLOGY, AVERAGE_MARK);
    }

}
