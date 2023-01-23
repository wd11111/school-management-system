package pl.schoolmanagementsystem.schoolSubject.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.schoolmanagementsystem.common.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.model.SchoolSubject;

@Mapper(componentModel = "spring")
public interface SchoolSubjectMapper {

    @Mapping(source = "schoolSubjectDto.subjectName", target = "name")
    SchoolSubject mapDtoToEntity(SchoolSubjectDto schoolSubjectDto);
}
