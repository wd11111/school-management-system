package pl.schoolmanagementsystem.common.mark;

import pl.schoolmanagementsystem.common.mark.dto.MarkDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MarkMapper {

    public static List<Byte> mapListOfMarksToBytes(List<Mark> marks) {
        return marks.stream()
                .map(Mark::getMark)
                .collect(Collectors.toList());
    }

    public static Map<String, List<Byte>> mapToListOfBytesInMapStructure(Map<String, List<MarkDto>> mapToTransform) {
        return mapToTransform.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        m -> m.getValue().stream()
                                .map(MarkDto::getMark)
                                .collect(Collectors.toList())));
    }

    public static Mark createMark(MarkDto markDto, long studentId) {
        return Mark.builder()
                .mark(markDto.getMark())
                .studentId(studentId)
                .subject(markDto.getSubject())
                .build();
    }
}
