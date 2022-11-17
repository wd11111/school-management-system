package pl.schoolmanagementsystem.teacher.service;

import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.common.teacher.dto.TeacherInputDto;
import pl.schoolmanagementsystem.common.teacher.dto.TeacherOutputDto;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface TeacherSamples extends StudentSamples{

    String ADAM = "Adam";
    String NOWAK = "Nowak";
    String ADAM_PASSWORD = "123456789";
    String BIOLOGY = "Biology";
    String HISTORY = "History";
    String KOWALCZYK = "Kowalczyk";
    String ALICJA = "Alicja";
    String CLASS2 = "3b";
    String ENGLISH = "english";
    int ID_1 = 1;
    int ID_2 = 2;

    default Teacher teacher1() {
        return Teacher.builder()
                .name(ADAM)
                .surname(NOWAK)
                .taughtSubjects(new HashSet<>())
                .build();

    }

    default Teacher savedTeacher1() {
        return Teacher.builder()
                .id(1)
                .name(ADAM)
                .surname(NOWAK)
                .build();

    }

    default Teacher teacher2() {
        return Teacher.builder()
                .name(ALICJA)
                .surname(KOWALCZYK)
                .taughtSubjects(new HashSet<>(Set.of(createSchoolSubject())))
                .build();
    }

    default Teacher savedTeacher2() {
        return Teacher.builder()
                .id(2)
                .name(ALICJA)
                .surname(KOWALCZYK)
                .taughtSubjects(new HashSet<>(Set.of(createSchoolSubject())))
                .build();
    }

    default List<Teacher> listOfTeachers() {
        return List.of(teacher1(), teacher2());
    }

    default SchoolSubject createSchoolSubject() {
        SchoolSubject schoolSubject = new SchoolSubject();
        schoolSubject.setName(BIOLOGY);
        return schoolSubject;
    }

    default SchoolClass createSchoolClass() {
        return SchoolClass.builder()
                .name(CLASS_A)
                .build();
    }

    default List<SubjectAndClassDto> listOfTaughtClasses() {
        return List.of(new SubjectAndClassDto(BIOLOGY, CLASS_A),
                new SubjectAndClassDto(ENGLISH, CLASS_A),
                new SubjectAndClassDto(HISTORY, CLASS2));
    }

    default TeacherOutputDto teacherOutput1() {
        return TeacherOutputDto.builder()
                .id(ID_1)
                .name(ADAM)
                .surname(NOWAK)
                .taughtSubjects(Collections.emptySet())
                .build();
    }

    default TeacherOutputDto teacherOutput2() {
        return TeacherOutputDto.builder()
                .id(ID_2)
                .name(ALICJA)
                .surname(KOWALCZYK)
                .taughtSubjects(Set.of(BIOLOGY))
                .build();
    }

    default TeacherInputDto teacherInput1() {
        return TeacherInputDto.builder()
                .email(ADAM)
                .name(ADAM)
                .surname(NOWAK)
                .isAdmin(true)
                .taughtSubjects(Collections.emptySet())
                .build();
    }
}
