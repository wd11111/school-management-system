package pl.schoolmanagementsystem.schoolSubject.utlis;

import pl.schoolmanagementsystem.common.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.model.SchoolSubject;
import pl.schoolmanagementsystem.schoolSubject.utils.SchoolSubjectMapper;

public class SchoolSubjectMapperStub implements SchoolSubjectMapper {

    @Override
    public SchoolSubject mapDtoToEntity(SchoolSubjectDto schoolSubjectDto) {
        return SchoolSubject.builder()
                .name(schoolSubjectDto.getSubjectName())
                .build();
    }
}
