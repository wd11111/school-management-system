package pl.schoolmanagementsystem.common.criteria;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.schoolmanagementsystem.BaseContainerTest;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.exception.FilterException;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.common.repository.StudentRepository;
import pl.schoolmanagementsystem.student.search.StudentSearcherCriteriaApi;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class CriteriaApiFilterServiceTest extends BaseContainerTest implements Samples {

    @Autowired
    StudentRepository studentRepository;
    private StudentSearcherCriteriaApi studentSearcherCriteriaApi;

    @BeforeEach
    public void setUp() {
        CriteriaApiFilterService<Student> criteriaApiFilterService = new CriteriaApiFilterService<>();

        studentSearcherCriteriaApi = new StudentSearcherCriteriaApi(studentRepository, criteriaApiFilterService);
    }

    @Test
    void should_return_one_student_when_searching_by_id() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("id", "1", OperationType.EQUAL));

        List<Student> studentSearchDtos = studentSearcherCriteriaApi.searchStudent(searchRequestDtos);

        assertThat(studentSearchDtos.size()).isEqualTo(1);
    }

    @Test
    void should_return_two_students_when_searching_by_class() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("schoolClass", "1a", OperationType.EQUAL));

        List<Student> studentSearchDtos = studentSearcherCriteriaApi.searchStudent(searchRequestDtos);

        assertThat(studentSearchDtos.size()).isEqualTo(2);
    }

    @Test
    void should_return_one_student_when_searching_by_class_and_name() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("schoolClass", "1a", OperationType.EQUAL),
                new SearchRequestDto("name", "studentName1", OperationType.EQUAL));

        List<Student> studentSearchDtos = studentSearcherCriteriaApi.searchStudent(searchRequestDtos);

        assertThat(studentSearchDtos.size()).isEqualTo(1);
    }

    @Test
    void should_return_one_student_when_searching_by_name_using_like_operation() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("name", "2", OperationType.LIKE));

        List<Student> studentSearchDtos = studentSearcherCriteriaApi.searchStudent(searchRequestDtos);

        assertThat(studentSearchDtos.size()).isEqualTo(1);
    }

    @Test
    void should_return_two_students_when_searching_by_id_using_number_between_operation() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("id", "1to2", OperationType.NUMBER_BETWEEN));

        List<Student> studentSearchDtos = studentSearcherCriteriaApi.searchStudent(searchRequestDtos);

        assertThat(studentSearchDtos.size()).isEqualTo(2);
    }

    @Test
    void should_return_one_student_when_searching_by_id_using_number_between_operation() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("id", "1to1", OperationType.NUMBER_BETWEEN));

        List<Student> studentSearchDtos = studentSearcherCriteriaApi.searchStudent(searchRequestDtos);

        assertThat(studentSearchDtos.size()).isEqualTo(1);
    }

    @Test
    void should_return_two_students_when_searching_by_date_using_between_operation() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("birthDate", "2002-01-01to2002-03-01", OperationType.DATE_BETWEEN));

        List<Student> studentSearchDtos = studentSearcherCriteriaApi.searchStudent(searchRequestDtos);

        assertThat(studentSearchDtos.size()).isEqualTo(2);
    }

    @Test
    void should_return_one_student_when_searching_by_date_using_between_operation() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("birthDate", "2002-01-01to2002-01-30", OperationType.DATE_BETWEEN));

        List<Student> studentSearchDtos = studentSearcherCriteriaApi.searchStudent(searchRequestDtos);

        assertThat(studentSearchDtos.size()).isEqualTo(1);
    }

    @Test
    void should_throw_exception_when_local_date_format_is_wrong() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("birthDate", "01-01-2002to2002-01-30", OperationType.DATE_BETWEEN));

        assertThatThrownBy(() -> studentSearcherCriteriaApi.searchStudent(searchRequestDtos))
                .isInstanceOf(FilterException.class)
                .hasMessage("Wrong date pattern! Use: yyyy-MM-dd");
    }

    @Test
    void should_throw_exception_when_regex_is_not_correct() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("birthDate", "2002-01-01from2002-01-30", OperationType.DATE_BETWEEN));

        assertThatThrownBy(() -> studentSearcherCriteriaApi.searchStudent(searchRequestDtos))
                .isInstanceOf(FilterException.class)
                .hasMessage("Wrong split regex! Use: to");
    }

    @Test
    void should_throw_exception_when_splitted_array_size_is_not_2() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("birthDate", "2002-01-01to2002-01-30to2003-01-01", OperationType.DATE_BETWEEN));

        assertThatThrownBy(() -> studentSearcherCriteriaApi.searchStudent(searchRequestDtos))
                .isInstanceOf(FilterException.class)
                .hasMessage("Wrong format!");
    }

    @Test
    void should_throw_exception_when_value_can_not_be_parsed_to_number() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("id", "1toa", OperationType.NUMBER_BETWEEN));

        assertThatThrownBy(() -> studentSearcherCriteriaApi.searchStudent(searchRequestDtos))
                .isInstanceOf(FilterException.class)
                .hasMessage("Value does not contain a parsable number!");
    }
}