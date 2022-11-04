package pl.schoolmanagementsystem.mapper;

import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.model.dto.SchoolClassDto;

public class SchoolClassMapper {

    public static SchoolClassDto mapSchoolClassToDto(SchoolClass schoolClass) {
        return new SchoolClassDto(schoolClass.getSchoolClassName());
    }
}
