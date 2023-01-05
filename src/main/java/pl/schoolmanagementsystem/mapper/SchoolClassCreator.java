package pl.schoolmanagementsystem.mapper;

import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.model.dto.SchoolClassDto;

public class SchoolClassCreator {

    public static SchoolClass createSchoolClass(SchoolClassDto schoolClassDto) {
        return SchoolClass.builder()
                .name(schoolClassDto.getSchoolClassName())
                .build();
    }
}
