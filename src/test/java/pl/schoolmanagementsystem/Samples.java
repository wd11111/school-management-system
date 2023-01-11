package pl.schoolmanagementsystem;

import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.common.teacher.TeacherInClass;
import pl.schoolmanagementsystem.common.user.AppUser;
import pl.schoolmanagementsystem.teacher.dto.StudentWithMarksDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    default StudentWithMarksDto createStudentWithMarksDto() {
        return new StudentWithMarksDto(ID_1 , NAME, SURNAME, new ArrayList<>());
    }

    default StudentWithMarksDto createStudentWithMarksDto2() {
        return new StudentWithMarksDto(ID_2 , NAME, SURNAME, new ArrayList<>());
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
}
