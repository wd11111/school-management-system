package pl.schoolmanagementsystem.admin.schoolClass.mapper;

import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.schoolClass.dto.SchoolClassDto;

public class SchoolClassCreator {

    public static SchoolClass createSchoolClass(SchoolClassDto schoolClassDto) {
        return SchoolClass.builder()
                .name(schoolClassDto.getSchoolClassName())
                .build();
    }
}
