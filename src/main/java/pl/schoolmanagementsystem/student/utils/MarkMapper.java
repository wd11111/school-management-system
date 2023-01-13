package pl.schoolmanagementsystem.student.utils;

import pl.schoolmanagementsystem.common.dto.MarkDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MarkMapper {

    public static Map<String, List<BigDecimal>> mapListOfMarkDtoToDecimalsInMapStructure(Map<String, List<MarkDto>> mapToTransform) {
        return mapToTransform.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        m -> m.getValue().stream()
                                .map(MarkDto::getMark)
                                .collect(Collectors.toList())));
    }
}
