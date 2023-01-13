package pl.schoolmanagementsystem.student.utils;

import org.junit.jupiter.api.Test;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.dto.MarkDto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.schoolmanagementsystem.student.utils.MarkMapper.mapListOfMarkDtoToDecimalsInMapStructure;

class MarkMapperTest implements Samples {

    @Test
    void should_map_list_of_doubles_in_map_structure() {
        Map<String, List<MarkDto>> mapToTransform = new HashMap<>(
                Map.of(SUBJECT_BIOLOGY, List.of(createMarkDto1(), createMarkDto2())));
        Map<String, List<BigDecimal>> expected = new HashMap<>(Map.of(SUBJECT_BIOLOGY, List.of(BigDecimal.valueOf(4.0), BigDecimal.valueOf(2.0))));

        Map<String, List<BigDecimal>> result = mapListOfMarkDtoToDecimalsInMapStructure(mapToTransform);

        assertThat(result).containsExactlyEntriesOf(expected);
    }

}