package pl.schoolmanagementsystem.teacher.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.schoolmanagementsystem.common.model.Mark;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface MarkMapperMapstruct {

    @Mapping(source = "mark", target = "mark")
    @Mapping(source = "studentId", target = "studentId")
    @Mapping(source = "subject", target = "subject")
    Mark mapToEntity(BigDecimal mark, Long studentId, String subject);

}
