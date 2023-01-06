package pl.schoolmanagementsystem.schoolSubject.mapper;

import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;

public class SchoolSubjectCreator {

    public static SchoolSubject createSchoolSubject(SchoolSubjectDto schoolSubjectDto) {
        return SchoolSubject.builder()
                .name(schoolSubjectDto.getSubjectName())
                .build();
    }
}
