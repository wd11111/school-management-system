package pl.schoolmanagementsystem.mapper;

import pl.schoolmanagementsystem.model.Mark;
import pl.schoolmanagementsystem.model.dto.output.MarkOutputDto;

public class MarkMapper {

    public static MarkOutputDto mapMarkToOutputDto(Mark mark) {
        return new MarkOutputDto(mark.getMarkId(), mark.getMark(), mark.getStudent().getStudentId());
    }
}
