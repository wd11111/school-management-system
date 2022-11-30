package pl.schoolmanagementsystem.common.mark;

import pl.schoolmanagementsystem.common.mark.dto.MarkInputDto;
import pl.schoolmanagementsystem.common.mark.dto.MarkWithTwoFields;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class MarkMapper {

    public static List<Byte> mapListOfMarksToBytes(List<Mark> marks) {
        return marks.stream()
                .map(Mark::getMark)
                .collect(Collectors.toList());
    }

    public static Map<String, List<Byte>> mapToListOfBytesInMapStructure(Map<String, List<MarkWithTwoFields>> mapToTransform) {
        Map<String, List<Byte>> resultMap = new HashMap<>();
        mapToTransform.forEach((key, value) -> resultMap.put(key, value
                .stream()
                .map(MarkWithTwoFields::getMark)
                .collect(Collectors.toList())));
        return resultMap;
    }

    public static Mark createMark(MarkInputDto markInputDto, long studentId) {
        return Mark.builder()
                .mark(markInputDto.getMark())
                .studentId(studentId)
                .subject(markInputDto.getSubject())
                .build();
    }

    public static Map<String, List<MarkWithTwoFields>> groupMarksBySubject(List<MarkWithTwoFields> studentsMarks) {
        return studentsMarks.stream()
                .collect(groupingBy(MarkWithTwoFields::getSubject));
    }
}
