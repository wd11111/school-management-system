package pl.schoolmanagementsystem.mapper;

import pl.schoolmanagementsystem.model.Mark;
import pl.schoolmanagementsystem.model.dto.output.MarkOutputDto;

import java.util.List;
import java.util.stream.Collectors;

public class MarkMapper {

    public static MarkOutputDto mapMarkToOutputDto(Mark mark) {
        return new MarkOutputDto(mark.getMarkId(), mark.getMark(), mark.getStudent().getStudentId());
    }

    public static List<Integer> mapListOfMarksToIntegers(List<Mark> marks) {
        return marks.stream()
                .map(mark -> mark.getMark())
                .collect(Collectors.toList());
    }
}
