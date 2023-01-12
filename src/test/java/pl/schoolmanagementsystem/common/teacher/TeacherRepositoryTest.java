package pl.schoolmanagementsystem.common.teacher;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndClassDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
@Sql("/scripts/init_db.sql")
class TeacherRepositoryTest {

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
    private TeacherRepository teacherRepository;

    @Test
    void should_find_teacher_by_id() {
        Teacher result = teacherRepository.findById(1L).get();

        assertThat(result).extracting("id", "name", "surname")
                .containsAll(List.of(1L, "teacherName1", "teacherSurname1"));
    }

    @Test
    void should_find_email_by_teacher_id() {
        String emailById = teacherRepository.findEmailById(1);

        assertThat(emailById).isEqualTo("email2");
    }

    @Test
    void should_return_all_teachers() {
        List<Teacher> result = teacherRepository.findAllAndFetchSubjects();

        assertThat(result).extracting("surname")
                .containsExactly("teacherSurname1",
                        "teacherSurname2");
    }

    @Test
    void should_return_taught_classes_by_teacher() {
        Pageable pageable = PageRequest.of(0, 5);

        Page<SubjectAndClassDto> result = teacherRepository.findTaughtClassesByTeacher("email2", pageable);

        assertThat(result).extracting("schoolSubject", "schoolClass")
                .containsAll(List.of(tuple("biology", "1a"),
                        tuple("history", "1a")));
    }
}