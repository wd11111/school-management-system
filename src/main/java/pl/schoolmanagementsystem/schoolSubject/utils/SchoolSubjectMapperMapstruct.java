package pl.schoolmanagementsystem.schoolSubject.utils;

import org.mapstruct.Mapper;
import pl.schoolmanagementsystem.common.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.model.SchoolSubject;

@Mapper(componentModel = "spring")
public interface SchoolSubjectMapperMapstruct {

    SchoolSubject mapDtoToEntity(SchoolSubjectDto schoolSubjectDto);
}
