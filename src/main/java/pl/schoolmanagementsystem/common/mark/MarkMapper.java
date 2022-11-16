package pl.schoolmanagementsystem.common.mark;

import pl.schoolmanagementsystem.common.mark.dto.MarkDtoWithTwoFields;
import pl.schoolmanagementsystem.common.mark.dto.MarkInputDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MarkMapper {

    public static List<Integer> mapListOfMarksToIntegers(List<Mark> marks) {
        return marks.stream()
                .map(Mark::getMark)
                .collect(Collectors.toList());
    }

    public static Map<String, List<Integer>> mapToListOfIntegersInMapStructure(Map<String, List<MarkDtoWithTwoFields>> mapToTransform) {
        Map<String, List<Integer>> resultMap = new HashMap<>();
        mapToTransform.forEach((key, value) -> resultMap.put(key, value
                .stream()
                .map(MarkDtoWithTwoFields::getMark)
                .collect(Collectors.toList())));
        return resultMap;
    }

    public static Mark createMark(MarkInputDto markInputDto, int studentId) {
        return Mark.builder()
                .mark(markInputDto.getMark())
                .studentId(studentId)
                .subject(markInputDto.getSubject())
                .build();
    }

    public static Map<String, List<MarkDtoWithTwoFields>> groupMarksBySubject(List<MarkDtoWithTwoFields> studentsMarks) {
        return studentsMarks.stream()
                .collect(Collectors.groupingBy(MarkDtoWithTwoFields::getSubject));
    }
}
