package pl.schoolmanagementsystem.teacher.utils;

import pl.schoolmanagementsystem.common.model.Mark;

import java.math.BigDecimal;

public class MarkMapperStub implements MarkMapper {

    @Override
    public Mark mapToEntity(BigDecimal mark, Long studentId, String subject) {
        return Mark.builder()
                .mark(mark)
                .studentId(studentId)
                .subject(subject)
                .build();
    }
}
