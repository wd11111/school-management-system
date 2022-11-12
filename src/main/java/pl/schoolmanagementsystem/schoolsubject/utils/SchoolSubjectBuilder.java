package pl.schoolmanagementsystem.schoolsubject.utils;

import pl.schoolmanagementsystem.schoolsubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubject;

public class SchoolSubjectBuilder {

    public static SchoolSubject build(SchoolSubjectDto schoolSubjectDto) {
        return SchoolSubject.builder()
                .name(schoolSubjectDto.getSubjectName())
                .build();
    }
}
