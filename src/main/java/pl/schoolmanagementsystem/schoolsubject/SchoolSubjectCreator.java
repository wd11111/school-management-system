package pl.schoolmanagementsystem.schoolsubject;

import pl.schoolmanagementsystem.schoolsubject.dto.SchoolSubjectDto;

public class SchoolSubjectCreator {

    public static SchoolSubject build(SchoolSubjectDto schoolSubjectDto) {
        return SchoolSubject.builder()
                .name(schoolSubjectDto.getSubjectName())
                .build();
    }
}
