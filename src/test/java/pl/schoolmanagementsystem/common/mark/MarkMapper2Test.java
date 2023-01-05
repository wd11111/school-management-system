package pl.schoolmanagementsystem.common.mark;

import org.junit.jupiter.api.Test;
import pl.schoolmanagementsystem.model.dto.MarkDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.schoolmanagementsystem.mapper.MarkMapper2.mapToListOfBytesInMapStructure;

class MarkMapper2Test implements MarkSamples {

    @Test
    void should_map_list_of_bytes_to_map_structure() {
        Map<String, List<MarkDto>> mapToTransform = new HashMap<>(
                Map.of(SUBJECT, List.of(createMarkDto1(), createMarkDto2())));
        Map<String, List<Byte>> expected = new HashMap<>(Map.of(SUBJECT, List.of((byte) 4, (byte) 2)));

        Map<String, List<Byte>> result = mapToListOfBytesInMapStructure(mapToTransform);

        assertThat(result).containsExactlyEntriesOf(expected);
    }

}