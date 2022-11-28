package pl.schoolmanagementsystem;

import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndTeacherResponseDto;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.common.teacher.TeacherInClass;
import pl.schoolmanagementsystem.common.teacher.dto.TeacherOutputDto;
import pl.schoolmanagementsystem.common.user.AppUser;

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
    String ENGLISH = "english";
    String CLASS_1A = "1a";
    String CLASS_3B = "3b";
    int ID_1 = 1;
    int ID_2 = 2;

    default TeacherOutputDto createTeacherOutputDto() {
        return new TeacherOutputDto(ID_2, NAME3, SURNAME2, Set.of(SUBJECT_BIOLOGY));
    }

    default SubjectAndTeacherResponseDto createSubjectAndTeacherOutput2() {
        return new SubjectAndTeacherResponseDto(SUBJECT_HISTORY, NAME2, SURNAME);
    }

    default SchoolClass createSchoolClass() {
        return SchoolClass.builder()
                .name(CLASS_1A)
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
        return schoolSubject;
    }

    default List<SubjectAndClassDto> listOfTaughtClasses() {
        return List.of(new SubjectAndClassDto(SUBJECT_BIOLOGY, CLASS_1A),
                new SubjectAndClassDto(ENGLISH, CLASS_1A),
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
}
