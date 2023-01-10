package pl.schoolmanagementsystem.common.mark;

import org.junit.jupiter.api.Test;
import pl.schoolmanagementsystem.common.mark.dto.MarkDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.schoolmanagementsystem.student.utils.MarkMapper.mapToListOfDoublesinMapStructure;

class MarkMapperTest implements MarkSamples {

    @Test
    void should_map_list_of_bytes_to_map_structure() {
        Map<String, List<MarkDto>> mapToTransform = new HashMap<>(
                Map.of(SUBJECT, List.of(createMarkDto1(), createMarkDto2())));
        Map<String, List<Double>> expected = new HashMap<>(Map.of(SUBJECT, List.of(4.0, 2.0)));

        Map<String, List<Double>> result = mapToListOfDoublesinMapStructure(mapToTransform);

        assertThat(result).containsExactlyEntriesOf(expected);
    }

}