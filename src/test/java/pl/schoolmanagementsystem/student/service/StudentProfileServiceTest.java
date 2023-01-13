package pl.schoolmanagementsystem.student.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.dto.TaughtSubjectDto;
import pl.schoolmanagementsystem.common.repository.MarkRepository;
import pl.schoolmanagementsystem.common.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.repository.StudentRepository;
import pl.schoolmanagementsystem.student.dto.MarkAvgDto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentProfileServiceTest implements Samples {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private MarkRepository markRepository;

    @Mock
    private SchoolSubjectRepository subjectRepository;

    @InjectMocks
    private StudentProfileService studentProfileService;

    @Test
    void should_return_marks_grouped_by_subject() {
        Map<String, List<BigDecimal>> expected = new HashMap<>(
                Map.of(SUBJECT_BIOLOGY, List.of(getMarkAsBigDecimal1()),
                        SUBJECT_HISTORY, List.of(getMarkAsBigDecimal2())));
        when(markRepository.findAllMarksForStudent(anyString()))
                .thenReturn(List.of(createMarkDto1(), createMarkDto2()));

        Map<String, List<BigDecimal>> result = studentProfileService.getGroupedMarksBySubject(NAME);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void should_return_all_averages_for_student() {
        List<MarkAvgDto> expectedResult = List.of(new MarkAvgDto(SUBJECT_BIOLOGY, 2.0));
        when(markRepository.findAllAveragesForStudent(NAME)).thenReturn(expectedResult);

        List<MarkAvgDto> result = studentProfileService.getAverageMarks(NAME);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void should_return_taught_subjects_in_students_class() {
        List<TaughtSubjectDto> expectedResult = List.of(createTaughtSubjectDto(), createTaughtSubjectDto());
        when(studentRepository.findStudentsClass(any())).thenReturn(CLASS_3B);
        when(subjectRepository.findTaughtSubjectsInClass(anyString())).thenReturn(expectedResult);

        List<TaughtSubjectDto> result = studentProfileService.getTaughtSubjectsInClass(NAME);

        assertThat(result).isEqualTo(expectedResult);
    }
}