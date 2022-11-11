package pl.schoolmanagementsystem.teacher.service;

import pl.schoolmanagementsystem.email.model.Email;
import pl.schoolmanagementsystem.schoolclass.model.SchoolClass;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto;
import pl.schoolmanagementsystem.schoolsubject.model.SchoolSubject;
import pl.schoolmanagementsystem.teacher.dto.TeacherInputDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherOutputDto;
import pl.schoolmanagementsystem.teacher.model.Teacher;

import java.util.Collections;
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
    String A = "1a";

    default List<SubjectAndClassOutputDto> listOfTaughtClassesByTeacher() {
        return List.of(new SubjectAndClassOutputDto(BIOLOGY, A),
                new SubjectAndClassOutputDto(HISTORY, A),
                new SubjectAndClassOutputDto(HISTORY, "3b"));
    }

    default Teacher adminTeacher() {
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

    default Teacher teacherOfBiology() {
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

    default TeacherOutputDto mappedCreatedTeacher() {
        return new TeacherOutputDto(1, ADAM, NOWAK, Collections.emptySet());
    }

    default TeacherInputDto TeacherInputDto() {
        return new TeacherInputDto(ADAM, ADAM, NOWAK, ADAM_PASSWORD, true, Collections.emptySet());
    }

    default List<TeacherOutputDto> listOfTeacherDto() {
        return List.of(mappedCreatedTeacher(), teacherOutputDto());
    }

    default TeacherOutputDto teacherOutputDto() {
        return TeacherOutputDto.builder()
                .id(2)
                .name(ALICJA)
                .surname(KOWALCZYK)
                .taughtSubjects(Set.of(HISTORY, BIOLOGY))
                .build();
    }

    default List<Teacher> listOfTeachers() {
        return List.of(adminTeacher(), teacherOfBiology());
    }

    default SchoolSubject schoolSubject() {
        SchoolSubject schoolSubject = new SchoolSubject();
        schoolSubject.setName(BIOLOGY);
        return schoolSubject;
    }
}
