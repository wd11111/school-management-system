package pl.schoolmanagementsystem.teacher.service;

import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto;

import java.util.List;

public interface TeacherSamples {

    default List<SubjectAndClassOutputDto> listOfTaughtClassesByTeacher() {
        return List.of(new SubjectAndClassOutputDto("biology", "1a"),
                new SubjectAndClassOutputDto("history", "1a"),
                new SubjectAndClassOutputDto("history", "3b"));
    }
}
