package pl.schoolmanagementsystem.teacher.utils;

import pl.schoolmanagementsystem.common.mark.Mark;

public class MarkMapper {

    public static Mark mapDtoToEntity(double mark, long studentId, String schoolSubject) {
        return Mark.builder()
                .mark(mark)
                .studentId(studentId)
                .subject(schoolSubject)
                .build();
    }
}
