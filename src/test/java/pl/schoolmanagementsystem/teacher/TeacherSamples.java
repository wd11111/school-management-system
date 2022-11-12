package pl.schoolmanagementsystem.teacher;

import pl.schoolmanagementsystem.email.Email;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubject;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface TeacherSamples {

    String ADAM = "Adam";
    String NOWAK = "Nowak";
    String ADAM_PASSWORD = "123456789";
    String BIOLOGY = "Biology";
    String HISTORY = "History";
    String KOWALCZYK = "Kowalczyk";
    String ALICJA = "Alicja";
    String CLASS1 = "1a";
    String CLASS2 = "3b";
    String ENGLISH = "english";
    int ID_1 = 1;
    int ID_2 = 2;

    default Teacher teacher1() {
        return Teacher.builder()
                .id(1)
                .name(ADAM)
                .surname(NOWAK)
                .password(ADAM_PASSWORD)
                .isAdmin(true)
                .email(new Email(ADAM))
                .taughtSubjects(new HashSet<>())
                .build();

    }

    default Teacher savedTeacher1() {
        return Teacher.builder()
                .id(1)
                .name(ADAM)
                .surname(NOWAK)
                .password(ADAM_PASSWORD)
                .isAdmin(true)
                .email(new Email(ADAM))
                .taughtSubjects(new HashSet<>())
                .build();

    }

    default Teacher teacher2() {
        return Teacher.builder()
                .name(ALICJA)
                .surname(KOWALCZYK)
                .password(ADAM_PASSWORD)
                .isAdmin(false)
                .email(new Email(ALICJA))
                .taughtSubjects(new HashSet<>(Set.of(schoolSubject())))
                .build();
    }

    default Teacher savedTeacher2() {
        return Teacher.builder()
                .id(2)
                .name(ALICJA)
                .surname(KOWALCZYK)
                .password(ADAM_PASSWORD)
                .isAdmin(false)
                .email(new Email(ALICJA))
                .taughtSubjects(new HashSet<>(Set.of(schoolSubject())))
                .build();
    }

    default List<Teacher> listOfTeachers() {
        return List.of(teacher1(), teacher2());
    }

    default SchoolSubject schoolSubject() {
        SchoolSubject schoolSubject = new SchoolSubject();
        schoolSubject.setName(BIOLOGY);
        return schoolSubject;
    }

    default List<SubjectAndClassOutputDto> listOfTaughtClasses() {
        return List.of(new SubjectAndClassOutputDto(BIOLOGY, CLASS1),
                new SubjectAndClassOutputDto(ENGLISH, CLASS1),
                new SubjectAndClassOutputDto(HISTORY, CLASS2));
    }
}