package pl.schoolmanagementsystem.student.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.schoolmanagementsystem.ControllerSamples;
import pl.schoolmanagementsystem.common.mark.MarkRepository;
import pl.schoolmanagementsystem.common.mark.MarkSamples;
import pl.schoolmanagementsystem.common.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.common.mark.dto.MarkDto;
import pl.schoolmanagementsystem.common.student.StudentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentMarkServiceTest implements ControllerSamples, MarkSamples {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private MarkRepository markRepository;

    @InjectMocks
    private StudentMarkService studentMarkService;

    @Test
    void should_return_marks_grouped_by_subject() {
        Map<String, List<MarkDto>> expected = new HashMap<>(
                Map.of(SUBJECT_BIOLOGY, List.of(createMarkDto1()),
                        SUBJECT_HISTORY, List.of(createMarkDto2())));
        when(markRepository.findAllMarksForStudent(anyString()))
                .thenReturn(List.of(createMarkDto1(), markWithTwoFields2()));

        Map<String, List<MarkDto>> result = studentMarkService.getGroupedMarksBySubject(NAME);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void should_return_all_averages_for_student() {
        List<MarkAvgDto> expectedResult = List.of(new MarkAvgDto(SUBJECT_BIOLOGY, MARK));
        when(studentRepository.findIdByEmail(anyString())).thenReturn(ID_1);
        when(markRepository.findAllAveragesForStudent(ID_1)).thenReturn(expectedResult);

        List<MarkAvgDto> result = studentMarkService.getAverageMarks(NAME);

        assertThat(result).isEqualTo(expectedResult);
    }
}