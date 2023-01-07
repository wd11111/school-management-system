package pl.schoolmanagementsystem.teacher.utils;

import pl.schoolmanagementsystem.common.mark.Mark;
import pl.schoolmanagementsystem.common.mark.dto.MarkDto;

public class MarkMapper {

    public static Mark mapDtoToEntity(MarkDto markDto, long studentId) {
        return Mark.builder()
                .mark(markDto.getMark())
                .studentId(studentId)
                .subject(markDto.getSubject())
                .build();
    }
}
