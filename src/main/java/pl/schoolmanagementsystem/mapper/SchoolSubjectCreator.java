package pl.schoolmanagementsystem.mapper;

import pl.schoolmanagementsystem.model.SchoolSubject;
import pl.schoolmanagementsystem.model.dto.SchoolSubjectDto;

public class SchoolSubjectCreator {

    public static SchoolSubject createSchoolSubject(SchoolSubjectDto schoolSubjectDto) {
        return SchoolSubject.builder()
                .name(schoolSubjectDto.getSubjectName())
                .build();
    }
}
