package pl.schoolmanagementsystem.common.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.schoolmanagementsystem.BaseContainerTest;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.student.dto.MarkAvgDto;
import pl.schoolmanagementsystem.student.dto.MarkDto;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class MarkRepositoryTest extends BaseContainerTest implements Samples {

    @Autowired
    private MarkRepository markRepository;

    @Test
    void should_return_all_marks_for_student() {
        List<MarkDto> marks = markRepository.findAllMarksForStudent("jan.kowalski.1@example.com");

        assertThat(marks).extracting("mark")
                .containsAll(List.of(
                        new BigDecimal("4.00"),
                        new BigDecimal("1.00"),
                        new BigDecimal("2.00")));
    }

    @Test
    void should_return_all_averages_for_student() {
        List<MarkAvgDto> allAveragesForStudent = markRepository.findAllAveragesForStudent("jan.kowalski.1@example.com");

        assertThat(allAveragesForStudent).extracting("subject", "avg")
                .containsAll(List.of(
                        tuple("biology", 2.5),
                        tuple("history", 2.0)));
    }

}