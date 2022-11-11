package pl.schoolmanagementsystem.teacher.service;

import pl.schoolmanagementsystem.email.model.Email;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherInputDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherOutputDto;
import pl.schoolmanagementsystem.teacher.model.Teacher;

import java.util.Collections;
import java.util.List;

public interface TeacherSamples {

    String SAMPLE_EMAIL = "adamnowak@gmail.com";
    String SAMPLE_NAME = "Adam";
    String SAMPLE_SURNAME = "Nowak";
    String SAMPLE_PASSWORD = "123456789";

    default List<SubjectAndClassOutputDto> listOfTaughtClassesByTeacher() {
        return List.of(new SubjectAndClassOutputDto("biology", "1a"),
                new SubjectAndClassOutputDto("history", "1a"),
                new SubjectAndClassOutputDto("history", "3b"));
    }

    default Teacher createdTeacher() {
        return Teacher.builder()
                .id(1)
                .name(SAMPLE_NAME)
                .surname(SAMPLE_SURNAME)
                .password(SAMPLE_PASSWORD)
                .isAdmin(true)
                .email(new Email(SAMPLE_EMAIL))
                .build();

    }

    default TeacherOutputDto mappedCreatedTeacher() {
        return new TeacherOutputDto(1, SAMPLE_NAME, SAMPLE_SURNAME, Collections.emptySet());
    }

    default TeacherInputDto sampleTeacherInputDto() {
        return new TeacherInputDto(SAMPLE_EMAIL, SAMPLE_NAME, SAMPLE_SURNAME, SAMPLE_PASSWORD, true, Collections.emptySet());
    }
}
