package pl.schoolmanagementsystem.schoolSubject.mapper;

import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;

public class SchoolSubjectMapper {

    public static SchoolSubject mapDtoToEntity(SchoolSubjectDto schoolSubjectDto) {
        return SchoolSubject.builder()
                .name(schoolSubjectDto.getSubjectName())
                .build();
    }
}
