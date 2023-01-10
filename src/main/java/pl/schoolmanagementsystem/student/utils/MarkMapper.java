package pl.schoolmanagementsystem.student.utils;

import pl.schoolmanagementsystem.common.mark.dto.MarkDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MarkMapper {

    public static Map<String, List<Double>> mapToListOfDoublesinMapStructure(Map<String, List<MarkDto>> mapToTransform) {
        return mapToTransform.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        m -> m.getValue().stream()
                                .map(MarkDto::getMark)
                                .collect(Collectors.toList())));
    }
}
