package pl.schoolmanagementsystem.admin.schoolClass.mapper;

import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.schoolClass.dto.SchoolClassDto;

public class SchoolClassMapper {

    public static SchoolClass createSchoolClass(SchoolClassDto schoolClassDto) {
        return SchoolClass.builder()
                .name(schoolClassDto.getSchoolClassName())
                .build();
    }
}
