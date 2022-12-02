package pl.schoolmanagementsystem.common.mark;

import pl.schoolmanagementsystem.common.mark.dto.MarkDto;

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

    public static Map<String, List<Byte>> mapToListOfBytesInMapStructure(Map<String, List<MarkDto>> mapToTransform) {
        Map<String, List<Byte>> resultMap = new HashMap<>();
        mapToTransform.forEach((key, value) -> resultMap.put(key, value
                .stream()
                .map(MarkDto::getMark)
                .collect(Collectors.toList())));
        return resultMap;
    }

    public static Mark createMark(MarkDto markDto, long studentId) {
        return Mark.builder()
                .mark(markDto.getMark())
                .studentId(studentId)
                .subject(markDto.getSubject())
                .build();
    }

    public static Map<String, List<MarkDto>> groupMarksBySubject(List<MarkDto> studentsMarks) {
        return studentsMarks.stream()
                .collect(groupingBy(MarkDto::getSubject));
    }
}
