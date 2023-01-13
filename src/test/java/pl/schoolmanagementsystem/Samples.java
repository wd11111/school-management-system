package pl.schoolmanagementsystem;

import pl.schoolmanagementsystem.common.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.common.mark.dto.MarkDto;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.schoolClass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.TaughtSubjectDto;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.common.student.dto.StudentDto;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.common.teacher.TeacherInClass;
import pl.schoolmanagementsystem.common.user.AppUser;
import pl.schoolmanagementsystem.schoolClass.dto.AddTeacherToClassDto;
import pl.schoolmanagementsystem.schoolClass.dto.TeacherInClassDto;
import pl.schoolmanagementsystem.student.dto.CreateStudentDto;
import pl.schoolmanagementsystem.teacher.dto.CreateTeacherDto;
import pl.schoolmanagementsystem.teacher.dto.StudentWithMarksDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherDto;

import java.math.BigDecimal;
import java.util.*;

public interface Samples {

    String NAME = "Hubert";
    String NAME2 = "Adam";
    String NAME3 = "Alicja";
    String SURNAME = "Nowak";
    String SURNAME2 = "Kowalczyk";

    String SUBJECT_BIOLOGY = "Biology";
    String SUBJECT_HISTORY = "History";
    String SUBJECT_ENGLISH = "english";
    String CLASS_1A = "1a";
    String CLASS_3B = "3b";
    long ID_1 = 1;
    long ID_2 = 2;

    boolean IS_ADMIN = false;

    double AVERAGE_MARK_3_0 = 3.0;

    default TeacherDto createTeacherDto() {
        return new TeacherDto(ID_2, NAME3, SURNAME2, Set.of(SUBJECT_BIOLOGY));
    }

    default SchoolClass createSchoolClass() {
        return SchoolClass.builder()
                .name(CLASS_1A)
                .students(new HashSet<>())
                .teachersInClass(new HashSet<>())
                .build();
    }

    default Teacher createTeacherNoSubjectsTaught() {
        return Teacher.builder()
                .id(ID_1)
                .name(NAME2)
                .surname(SURNAME)
                .taughtSubjects(new HashSet<>())
                .appUser(getAppUser())
                .build();
    }

    default Teacher createTeacherOfBiology() {
        return Teacher.builder()
                .id(ID_2)
                .name(NAME3)
                .surname(SURNAME2)
                .taughtSubjects(new HashSet<>(Set.of(createSchoolSubject())))
                .build();
    }

    default SchoolSubject createSchoolSubject() {
        SchoolSubject schoolSubject = SchoolSubject.builder().build();
        schoolSubject.setName(SUBJECT_BIOLOGY);
        schoolSubject.setTeachersInClasses(new HashSet<>());
        return schoolSubject;
    }

    default List<SubjectAndClassDto> listOfTaughtClasses() {
        return List.of(new SubjectAndClassDto(SUBJECT_BIOLOGY, CLASS_1A),
                new SubjectAndClassDto(SUBJECT_ENGLISH, CLASS_1A),
                new SubjectAndClassDto(SUBJECT_HISTORY, CLASS_3B));
    }

    default Student createStudent() {
        return Student.builder()
                .id(ID_1)
                .name(NAME)
                .schoolClass(CLASS_1A)
                .marks(new ArrayList<>())
                .build();
    }

    default Student createStudent2() {
        return Student.builder()
                .id(ID_2)
                .name(NAME)
                .schoolClass(CLASS_1A)
                .appUser(getAppUser())
                .marks(new ArrayList<>())
                .build();
    }

    default SchoolClass createSchoolClassWithTeacher() {
        SchoolClass schoolClass = createSchoolClass();
        TeacherInClass teacherInClass = TeacherInClass.builder()
                .teacher(createTeacherOfBiology())
                .taughtSubject(SUBJECT_BIOLOGY)
                .taughtClasses(new HashSet<>(Set.of(schoolClass)))
                .build();
        schoolClass.getTeachersInClass().add(teacherInClass);
        return schoolClass;
    }

    default AppUser getAppUser() {
        AppUser appUser = new AppUser();
        appUser.setToken("aaa");
        return appUser;
    }

    default MarkDto createMarkDto1() {
        return new MarkDto(BigDecimal.valueOf(4.0), SUBJECT_BIOLOGY);
    }

    default MarkDto createMarkDto2() {
        return new MarkDto(BigDecimal.valueOf(2.0), SUBJECT_HISTORY);
    }

    default BigDecimal getMarkAsBigDecimal1() {
        return BigDecimal.valueOf(4.0);
    }

    default BigDecimal getMarkAsBigDecimal2() {
        return BigDecimal.valueOf(2.0);
    }

    default MarkAvgDto markAvgDto() {
        return new MarkAvgDto(SUBJECT_BIOLOGY, AVERAGE_MARK_3_0);
    }

    default Map<String, List<BigDecimal>> getGroupedMarksBySubject() {
        return new HashMap<>(Map.of(
                SUBJECT_BIOLOGY, List.of(getMarkAsBigDecimal2(), getMarkAsBigDecimal2()),
                SUBJECT_HISTORY, List.of(getMarkAsBigDecimal2(), getMarkAsBigDecimal2())
        ));
    }

    default StudentWithMarksDto studentResponseDto3() {
        return new StudentWithMarksDto(ID_1, NAME, SURNAME, List.of(BigDecimal.ONE, BigDecimal.ONE));
    }

    default SchoolClassDto schoolClassDto() {
        return new SchoolClassDto(CLASS_1A);
    }

    default StudentDto studentResponseDto2() {
        return new StudentDto(ID_1, NAME, SURNAME);
    }

    default TaughtSubjectDto createTaughtSubjectDto() {
        return new TaughtSubjectDto(SUBJECT_BIOLOGY, NAME, SURNAME);
    }

    default AddTeacherToClassDto teacherInClassRequest() {
        return new AddTeacherToClassDto(ID_1, SUBJECT_BIOLOGY);
    }

    default TeacherInClassDto teacherInClassResponse() {
        return new TeacherInClassDto(ID_1, SUBJECT_BIOLOGY, Set.of(CLASS_1A));
    }

    default SchoolSubjectDto schoolSubjectDto() {
        return new SchoolSubjectDto(CLASS_1A);
    }

    default CreateStudentDto studentRequestDto() {
        return new CreateStudentDto(NAME, SURNAME, CLASS_1A, NAME);
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
