package pl.schoolmanagementsystem.mark.uitls;

import pl.schoolmanagementsystem.mark.model.Mark;
import pl.schoolmanagementsystem.mark.dto.MarkOutputDto;

import java.util.List;
import java.util.stream.Collectors;

public class MarkMapper {

    public static MarkOutputDto mapMarkToOutputDto(Mark mark) {
        return new MarkOutputDto(mark.getId(), mark.getMark(), mark.getStudent().getId());
    }

    public static List<Integer> mapListOfMarksToIntegers(List<Mark> marks) {
        return marks.stream()
                .map(mark -> mark.getMark())
                .collect(Collectors.toList());
    }
}
