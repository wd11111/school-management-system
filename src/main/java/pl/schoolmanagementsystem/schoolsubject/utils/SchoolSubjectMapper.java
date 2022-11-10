package pl.schoolmanagementsystem.schoolsubject.utils;

import pl.schoolmanagementsystem.schoolsubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.schoolsubject.model.SchoolSubject;

public class SchoolSubjectMapper {

    public static SchoolSubjectDto mapSubjectToSubjectDto(SchoolSubject subject) {
        return new SchoolSubjectDto(subject.getName());
    }
}
