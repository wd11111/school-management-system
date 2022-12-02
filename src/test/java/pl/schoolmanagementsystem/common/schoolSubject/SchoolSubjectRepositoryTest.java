package pl.schoolmanagementsystem.common.schoolSubject;

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
import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndTeacherResponseDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
@Sql("/scripts/init_db.sql")
class SchoolSubjectRepositoryTest {

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
    private SchoolSubjectRepository schoolSubjectRepository;

    @Test
    void should_return_all_school_subjects() {
        Pageable pageable = PageRequest.of(0, 5);

        Page<SchoolSubjectDto> result = schoolSubjectRepository.findAllSchoolSubjects(pageable);

        assertThat(result).extracting("subjectName").containsAll(List.of("biology", "history"));
    }

    @Test
    void should_return_all_taught_subjects_in_school_class() {
        List<SubjectAndTeacherResponseDto> result = schoolSubjectRepository.findTaughtSubjectsInClass("1a");

        assertThat(result).extracting("subject", "teacherName", "teacherSurname")
                .containsAll(List.of(tuple("biology", "teacherName1", "teacherSurname1"),
                        tuple("history", "teacherName1", "teacherSurname1")));
    }
}