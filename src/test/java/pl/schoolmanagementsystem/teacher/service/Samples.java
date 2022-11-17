package pl.schoolmanagementsystem.teacher.service;

import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndTeacherOutputDto;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.common.teacher.TeacherInClass;
import pl.schoolmanagementsystem.common.teacher.dto.TeacherInputDto;
import pl.schoolmanagementsystem.common.teacher.dto.TeacherOutputDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface Samples {

    String TEACHER_NAME1 = "Hubert";
    String TEACHER_NAME2 = "Adam";
    String TEACHER_NAME3 = "Alicja";
    String TEACHER_SURNAME = "Nowak";
    String TEACHER_SURNAME2 = "Kowalczyk";
    String SUBJECT_BIOLOGY = "Biology";
    String SUBJECT_HISTORY = "History";
    String ENGLISH = "english";
    String CLASS_1A = "1a";
    String CLASS_3B = "3b";
    int ID_1 = 1;
    int ID_2 = 2;

    default SubjectAndTeacherOutputDto createSubjectAndTeacherOutput() {
        return new SubjectAndTeacherOutputDto(SUBJECT_BIOLOGY, TEACHER_NAME2, TEACHER_SURNAME);
    }

    default SubjectAndTeacherOutputDto createSubjectAndTeacherOutput2() {
        return new SubjectAndTeacherOutputDto(SUBJECT_HISTORY, TEACHER_NAME2, TEACHER_SURNAME);
    }

    default SchoolClass createSchoolClass() {
        return SchoolClass.builder()
                .name(CLASS_1A)
                .teachersInClass(new HashSet<>())
                .build();
    }

    default Teacher teacherNoId1() {
        return Teacher.builder()
                .name(TEACHER_NAME2)
                .surname(TEACHER_SURNAME)
                .taughtSubjects(new HashSet<>())
                .build();

    }

    default Teacher teacherWithId1() {
        return Teacher.builder()
                .id(1)
                .name(TEACHER_NAME2)
                .surname(TEACHER_SURNAME)
                .taughtSubjects(Collections.emptySet())
                .build();

    }

    default Teacher teacherNoId2() {
        return Teacher.builder()
                .name(TEACHER_NAME3)
                .surname(TEACHER_SURNAME2)
                .taughtSubjects(new HashSet<>(Set.of(createSchoolSubject())))
                .build();
    }

    default Teacher teacherWithId2() {
        return Teacher.builder()
                .id(2)
                .name(TEACHER_NAME3)
                .surname(TEACHER_SURNAME2)
                .taughtSubjects(new HashSet<>(Set.of(createSchoolSubject())))
                .build();
    }

    default List<Teacher> listOfTeachers() {
        return List.of(teacherNoId1(), teacherNoId2());
    }

    default SchoolSubject createSchoolSubject() {
        SchoolSubject schoolSubject = new SchoolSubject();
        schoolSubject.setName(SUBJECT_BIOLOGY);
        return schoolSubject;
    }

    default List<SubjectAndClassDto> listOfTaughtClasses() {
        return List.of(new SubjectAndClassDto(SUBJECT_BIOLOGY, CLASS_1A),
                new SubjectAndClassDto(ENGLISH, CLASS_1A),
                new SubjectAndClassDto(SUBJECT_HISTORY, CLASS_3B));
    }

    default TeacherOutputDto teacherOutput1() {
        return TeacherOutputDto.builder()
                .id(ID_1)
                .name(TEACHER_NAME2)
                .surname(TEACHER_SURNAME)
                .taughtSubjects(Collections.emptySet())
                .build();
    }

    default TeacherOutputDto teacherOutput2() {
        return TeacherOutputDto.builder()
                .id(ID_2)
                .name(TEACHER_NAME3)
                .surname(TEACHER_SURNAME2)
                .taughtSubjects(Set.of(SUBJECT_BIOLOGY))
                .build();
    }

    default TeacherInputDto teacherInput1() {
        return TeacherInputDto.builder()
                .email(TEACHER_NAME2)
                .name(TEACHER_NAME2)
                .surname(TEACHER_SURNAME)
                .isAdmin(true)
                .taughtSubjects(Collections.emptySet())
                .build();
    }

    default Student createStudent() {
        return Student.builder()
                .id(ID_1)
                .name(TEACHER_NAME1)
                .schoolClass(CLASS_1A)
                .marks(new ArrayList<>())
                .build();
    }

    default Student createStudent2() {
        return Student.builder()
                .id(ID_2)
                .name(TEACHER_NAME1)
                .schoolClass(CLASS_1A)
                .marks(new ArrayList<>())
                .build();
    }

    default SchoolClass createSchoolClassWithTeacher() {
        SchoolClass schoolClass = createSchoolClass();
        TeacherInClass teacherInClass = TeacherInClass.builder()
                .teacher(teacherWithId2())
                .taughtSubject(SUBJECT_BIOLOGY)
                .taughtClasses(new HashSet<>(Set.of(schoolClass)))
                .build();
        schoolClass.getTeachersInClass().add(teacherInClass);
        return schoolClass;
    }
}
