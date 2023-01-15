package pl.schoolmanagementsystem.schoolClass.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.schoolmanagementsystem.common.dto.SchoolClassDto;
import pl.schoolmanagementsystem.common.model.SchoolClass;

@Mapper(componentModel = "spring")
public interface SchoolClassMapperMapstruct {

    @Mapping(source = "schoolClassDto.schoolClassName", target = "name")
    SchoolClass mapDtoToEntity(SchoolClassDto schoolClassDto);
}
