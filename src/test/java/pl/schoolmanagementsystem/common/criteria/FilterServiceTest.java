package pl.schoolmanagementsystem.common.criteria;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.email.service.EmailSender;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.common.repository.AppUserRepository;
import pl.schoolmanagementsystem.common.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.common.repository.StudentRepository;
import pl.schoolmanagementsystem.common.role.RoleAdder;
import pl.schoolmanagementsystem.student.dto.StudentSearchDto;
import pl.schoolmanagementsystem.student.service.AdminStudentService;
import pl.schoolmanagementsystem.student.utils.StudentMapper;
import pl.schoolmanagementsystem.student.utils.StudentMapperStub;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
@Sql("/scripts/init_db.sql")
public class FilterServiceTest implements Samples {

    public static final String POSTGRES = "postgres";

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:14.1")
                    .withDatabaseName(POSTGRES)
                    .withPassword(POSTGRES)
                    .withUsername(POSTGRES);

    @DynamicPropertySource
    public static void containerConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }


    @Autowired
    StudentRepository studentRepository;
    private AdminStudentService adminStudentService;

    @BeforeEach
    public void setUp() {
        SchoolClassRepository schoolClassRepository = mock(SchoolClassRepository.class);

        EmailSender emailSender = mock(EmailSender.class);

        AppUserRepository userRepository = mock(AppUserRepository.class);

        RoleAdder roleAdder = mock(RoleAdder.class);

        StudentMapper studentMapper = new StudentMapperStub();

        FilterService<Student> filterService = new FilterService<>();

        adminStudentService = new AdminStudentService(studentRepository, schoolClassRepository, emailSender, userRepository, roleAdder, studentMapper, filterService);
    }

    @Test
    void should_return_one_student_when_searching_by_id() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("id", "1", SearchRequestDto.Operation.EQUAL));

        List<StudentSearchDto> studentSearchDtos = adminStudentService.searchStudent(searchRequestDtos);

        assertThat(studentSearchDtos.size()).isEqualTo(1);
    }

    @Test
    void should_return_two_students_when_searching_by_class() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("schoolClass", "1a", SearchRequestDto.Operation.EQUAL));

        List<StudentSearchDto> studentSearchDtos = adminStudentService.searchStudent(searchRequestDtos);

        assertThat(studentSearchDtos.size()).isEqualTo(2);
    }

    @Test
    void should_return_one_student_when_searching_by_class_and_name() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("schoolClass", "1a", SearchRequestDto.Operation.EQUAL),
                new SearchRequestDto("name", "studentName1", SearchRequestDto.Operation.EQUAL));

        List<StudentSearchDto> studentSearchDtos = adminStudentService.searchStudent(searchRequestDtos);

        assertThat(studentSearchDtos.size()).isEqualTo(1);
    }

    @Test
    void should_return_one_student_when_searching_by_name_using_like_operation() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("name", "2", SearchRequestDto.Operation.LIKE));

        List<StudentSearchDto> studentSearchDtos = adminStudentService.searchStudent(searchRequestDtos);

        assertThat(studentSearchDtos.size()).isEqualTo(1);
    }

    @Test
    void should_return_two_students_when_searching_by_id_using_number_between_operation() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("id", "1to2", SearchRequestDto.Operation.NUMBER_BETWEEN));

        List<StudentSearchDto> studentSearchDtos = adminStudentService.searchStudent(searchRequestDtos);

        assertThat(studentSearchDtos.size()).isEqualTo(2);
    }

    @Test
    void should_return_one_student_when_searching_by_id_using_number_between_operation() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("id", "1to1", SearchRequestDto.Operation.NUMBER_BETWEEN));

        List<StudentSearchDto> studentSearchDtos = adminStudentService.searchStudent(searchRequestDtos);

        assertThat(studentSearchDtos.size()).isEqualTo(1);
    }

    @Test
    void should_return_two_students_when_searching_by_date_using_between_operation() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("birthDate", "2002-01-01to2002-03-01", SearchRequestDto.Operation.DATE_BETWEEN));

        List<StudentSearchDto> studentSearchDtos = adminStudentService.searchStudent(searchRequestDtos);

        assertThat(studentSearchDtos.size()).isEqualTo(2);
    }

    @Test
    void should_return_one_student_when_searching_by_date_using_between_operation() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("birthDate", "2002-01-01to2002-01-30", SearchRequestDto.Operation.DATE_BETWEEN));

        List<StudentSearchDto> studentSearchDtos = adminStudentService.searchStudent(searchRequestDtos);

        assertThat(studentSearchDtos.size()).isEqualTo(1);
    }

    @Test
    void should_throw_exception_when_local_date_format_is_wrong() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("birthDate", "01-01-2002to2002-01-30", SearchRequestDto.Operation.DATE_BETWEEN));

        assertThatThrownBy(() -> adminStudentService.searchStudent(searchRequestDtos))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Wrong date pattern! Use: yyyy-MM-dd");
    }

    @Test
    void should_throw_exception_when_regex_is_not_correct() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("birthDate", "2002-01-01from2002-01-30", SearchRequestDto.Operation.DATE_BETWEEN));

        assertThatThrownBy(() -> adminStudentService.searchStudent(searchRequestDtos))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Wrong split regex! Use: to");
    }

    @Test
    void should_throw_exception_when_splitted_array_size_is_not_2() {
        List<SearchRequestDto> searchRequestDtos = List.of(new SearchRequestDto("birthDate", "2002-01-01to2002-01-30to2003-01-01", SearchRequestDto.Operation.DATE_BETWEEN));

        assertThatThrownBy(() -> adminStudentService.searchStudent(searchRequestDtos))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Wrong format!");
    }
}