package pl.schoolmanagementsystem.mark.uitls;

import org.springframework.stereotype.Component;
import pl.schoolmanagementsystem.mark.dto.MarkDtoWithTwoFields;
import pl.schoolmanagementsystem.mark.dto.MarkOutputDto;
import pl.schoolmanagementsystem.mark.Mark;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class MarkMapper {

    public MarkOutputDto mapMarkToOutputDto(Mark mark) {
        return new MarkOutputDto(mark.getId(), mark.getMark(), mark.getStudent().getId(), mark.getSubject().getName());
    }

    public List<Integer> mapListOfMarksToIntegers(List<Mark> marks) {
        return marks.stream()
                .map(Mark::getMark)
                .collect(Collectors.toList());
    }

    public Map<String, List<Integer>> mapListOfMarksToIntegersInMapStructure(Map<String, List<MarkDtoWithTwoFields>> mapToTransform) {
        Map<String, List<Integer>> resultMap = new HashMap<>();
        mapToTransform.forEach((key, value) -> resultMap.put(key, value
                .stream()
                .map(MarkDtoWithTwoFields::getMark)
                .collect(Collectors.toList())));
        return resultMap;
    }
}
