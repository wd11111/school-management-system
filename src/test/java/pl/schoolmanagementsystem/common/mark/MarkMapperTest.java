package pl.schoolmanagementsystem.common.mark;

import org.junit.jupiter.api.Test;
import pl.schoolmanagementsystem.common.mark.dto.MarkDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.schoolmanagementsystem.common.mark.MarkMapper.groupMarksBySubject;
import static pl.schoolmanagementsystem.common.mark.MarkMapper.mapListOfMarksToBytes;
import static pl.schoolmanagementsystem.common.mark.MarkMapper.mapToListOfBytesInMapStructure;

class MarkMapperTest implements MarkSamples {

    @Test
    void should_map_list_of_marks_to_bytes() {
        List<Mark> listOfMarks = List.of(createMark(), createMark2());
        List<Byte> expected = List.of((byte) 2, (byte) 4);

        List<Byte> result = mapListOfMarksToBytes(listOfMarks);

        assertThat(result).containsAll(expected);
    }

    @Test
    void should_map_list_of_bytes_to_map_structure() {
        Map<String, List<MarkDto>> mapToTransform = new HashMap<>(
                Map.of(SUBJECT, List.of(createMarkDto1(), createMarkDto2())));
        Map<String, List<Byte>> expected = new HashMap<>(Map.of(SUBJECT, List.of((byte) 4, (byte) 2)));

        Map<String, List<Byte>> result = mapToListOfBytesInMapStructure(mapToTransform);

        assertThat(result).containsExactlyEntriesOf(expected);
    }

    @Test
    void should_group_marks_by_subject() {
        MarkDto mark1 = createMarkDto1();
        MarkDto mark2 = createMarkDto2();
        List<MarkDto> listOfMarksWithTwoFields = List.of(mark1, mark2);
        Map<String, List<MarkDto>> expected = new HashMap<>(
                Map.of(SUBJECT, List.of(mark1),
                        SUBJECT_2, List.of(mark2)));

        Map<String, List<MarkDto>> result = groupMarksBySubject(listOfMarksWithTwoFields);

        assertThat(result).containsAllEntriesOf(expected);
    }
}