package pl.schoolmanagementsystem.common.mark;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.schoolmanagementsystem.DataJpaTestBase;
import pl.schoolmanagementsystem.common.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.common.mark.dto.MarkWithTwoFields;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class MarkRepositoryTest extends DataJpaTestBase implements MarkSamples {

    @Autowired
    private MarkRepository markRepository;

    @Test
    void should_return_all_marks_for_student() {
        List<MarkWithTwoFields> marks = markRepository.findAllMarksForStudent("email");

        assertThat(marks).extracting("mark").containsAll(List.of(1, 2, 4));
    }

    @Test
    void should_return_all_averages_for_student() {
        List<MarkAvgDto> allAveragesForStudent = markRepository.findAllAveragesForStudent(3);

        assertThat(allAveragesForStudent).extracting("subject", "avg")
                .containsAll(List.of(tuple("biology", 2.5),
                        tuple("history", 2.0)));
    }

}