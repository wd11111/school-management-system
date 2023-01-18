package pl.schoolmanagementsystem.schoolClass.utils;

import pl.schoolmanagementsystem.common.dto.SchoolClassDto;
import pl.schoolmanagementsystem.common.model.SchoolClass;

public class SchoolClassMapperStub implements SchoolClassMapper {

    @Override
    public SchoolClass mapDtoToEntity(SchoolClassDto schoolClassDto) {
        return SchoolClass.builder()
                .name(schoolClassDto.getSchoolClassName())
                .build();
    }
}
