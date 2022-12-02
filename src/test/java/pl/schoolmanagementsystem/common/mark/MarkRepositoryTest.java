package pl.schoolmanagementsystem.common.mark;

import org.junit.jupiter.api.Test;
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
import pl.schoolmanagementsystem.common.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.common.mark.dto.MarkDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
//@DirtiesContext
@Sql("/scripts/init_db.sql")
class MarkRepositoryTest implements MarkSamples {

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
    private MarkRepository markRepository;

    @Test
    void should_return_all_marks_for_student() {
        List<MarkDto> marks = markRepository.findAllMarksForStudent("email");

        assertThat(marks).extracting("mark").containsAll(List.of((byte) 1, (byte) 2, (byte) 4));
    }

    @Test
    void should_return_all_averages_for_student() {
        List<MarkAvgDto> allAveragesForStudent = markRepository.findAllAveragesForStudent(1);

        assertThat(allAveragesForStudent).extracting("subject", "avg")
                .containsAll(List.of(tuple("biology", 2.5),
                        tuple("history", 2.0)));
    }

}