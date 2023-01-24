package pl.schoolmanagementsystem.schoolClass.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.schoolClass.dto.SchoolClassDto;

@Mapper(componentModel = "spring")
public interface SchoolClassMapper {

    @Mapping(source = "schoolClassDto.schoolClassName", target = "name")
    SchoolClass mapDtoToEntity(SchoolClassDto schoolClassDto);
}
