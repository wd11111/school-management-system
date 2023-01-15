package pl.schoolmanagementsystem.teacher.utils;

import pl.schoolmanagementsystem.common.model.Mark;

import java.math.BigDecimal;

public class MarkMapper {

    public static Mark mapDtoToEntity(BigDecimal mark, Long studentId, String schoolSubject) {
        return Mark.builder()
                .mark(mark)
                .studentId(studentId)
                .subject(schoolSubject)
                .build();
    }
}
