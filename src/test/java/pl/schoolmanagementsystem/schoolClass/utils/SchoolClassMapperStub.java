package pl.schoolmanagementsystem.schoolClass.utils;

import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.schoolClass.dto.SchoolClassDto;

public class SchoolClassMapperStub implements SchoolClassMapper {

    @Override
    public SchoolClass mapDtoToEntity(SchoolClassDto schoolClassDto) {
        return SchoolClass.builder()
                .name(schoolClassDto.getSchoolClassName())
                .build();
    }
}
