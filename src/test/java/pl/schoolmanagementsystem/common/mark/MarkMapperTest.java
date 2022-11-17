package pl.schoolmanagementsystem.common.mark;

import org.junit.jupiter.api.Test;
import pl.schoolmanagementsystem.common.mark.dto.MarkWithTwoFields;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.schoolmanagementsystem.common.mark.MarkMapper.groupMarksBySubject;
import static pl.schoolmanagementsystem.common.mark.MarkMapper.mapListOfMarksToIntegers;
import static pl.schoolmanagementsystem.common.mark.MarkMapper.mapToListOfIntegersInMapStructure;

class MarkMapperTest implements MarkSamples {

    @Test
    void should_map_list_of_marks_to_integers() {
        List<Mark> listOfMarks = List.of(createMark(), createMark2());
        List<Integer> expected = List.of(2, 4);

        List<Integer> result = mapListOfMarksToIntegers(listOfMarks);

        assertThat(result).containsAll(expected);
    }

    @Test
    void should_map_list_of_integers_to_map_structure() {
        Map<String, List<MarkWithTwoFields>> mapToTransform = new HashMap<>(
                Map.of(SUBJECT, List.of(createMarkWithTwoFields(), createMarkWithTwoFields2())));
        Map<String, List<Integer>> expected = new HashMap<>(Map.of(SUBJECT, List.of(4, 2)));

        Map<String, List<Integer>> result = mapToListOfIntegersInMapStructure(mapToTransform);

        assertThat(result).containsExactlyEntriesOf(expected);
    }

    @Test
    void should_group_marks_by_subject() {
        MarkWithTwoFields mark1 = createMarkWithTwoFields();
        MarkWithTwoFields mark2 = createMarkWithTwoFields2();
        List<MarkWithTwoFields> listOfMarksWithTwoFields = List.of(mark1, mark2);
        Map<String, List<MarkWithTwoFields>> expected = new HashMap<>(
                Map.of(SUBJECT, List.of(mark1),
                        SUBJECT_2, List.of(mark2)));

        Map<String, List<MarkWithTwoFields>> result = groupMarksBySubject(listOfMarksWithTwoFields);

        assertThat(result).containsAllEntriesOf(expected);
    }
}