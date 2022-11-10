package pl.schoolmanagementsystem.schoolclass.utils;

import pl.schoolmanagementsystem.schoolclass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.schoolclass.model.SchoolClass;

public class SchoolClassBuilder {

    public static SchoolClass build(SchoolClassDto schoolClassDto) {
        return SchoolClass.builder()
                .name(schoolClassDto.getSchoolClassName())
                .build();
    }
}
