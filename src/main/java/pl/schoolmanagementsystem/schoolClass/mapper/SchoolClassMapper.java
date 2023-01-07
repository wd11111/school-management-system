package pl.schoolmanagementsystem.schoolClass.mapper;

import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.schoolClass.dto.SchoolClassDto;

public class SchoolClassMapper {

    public static SchoolClass mapDtoToEntity(SchoolClassDto schoolClassDto) {
        return SchoolClass.builder()
                .name(schoolClassDto.getSchoolClassName())
                .build();
    }
}
