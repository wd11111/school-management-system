package pl.schoolmanagementsystem.schoolSubject.utils;

import pl.schoolmanagementsystem.common.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.model.SchoolSubject;

public class SchoolSubjectMapper {

    public static SchoolSubject mapDtoToEntity(SchoolSubjectDto schoolSubjectDto) {
        return SchoolSubject.builder()
                .name(schoolSubjectDto.getSubjectName())
                .build();
    }
}
