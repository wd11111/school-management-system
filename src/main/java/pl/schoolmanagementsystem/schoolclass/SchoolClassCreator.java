package pl.schoolmanagementsystem.schoolclass;

import pl.schoolmanagementsystem.schoolclass.dto.SchoolClassDto;

public class SchoolClassCreator {

    public static SchoolClass build(SchoolClassDto schoolClassDto) {
        return SchoolClass.builder()
                .name(schoolClassDto.getSchoolClassName())
                .build();
    }
}
