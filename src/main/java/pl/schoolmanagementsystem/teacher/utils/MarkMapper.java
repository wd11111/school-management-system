package pl.schoolmanagementsystem.teacher.utils;

import pl.schoolmanagementsystem.common.mark.Mark;

import java.math.BigDecimal;

public class MarkMapper {

    public static Mark mapDtoToEntity(BigDecimal mark, long studentId, String schoolSubject) {
        return Mark.builder()
                .mark(mark)
                .studentId(studentId)
                .subject(schoolSubject)
                .build();
    }
}
