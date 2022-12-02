package pl.schoolmanagementsystem.common.student;

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
import pl.schoolmanagementsystem.common.student.dto.StudentResponseDto2;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
@Sql("/scripts/init_db.sql")
class StudentRepositoryTest {

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
    private StudentRepository studentRepository;

    @Test
    void should_return_all_students_in_school_class() {
        List<StudentResponseDto2> result = studentRepository.findAllInClass("1a");

        assertThat(result).extracting("studentId", "name", "surname")
                .containsAll(List.of(tuple(1L, "studentName1", "studentSurname1"),
                        tuple(2L, "studentName2", "studentSurname2")));
    }

    @Test
    void should_return_all_students_in_school_class_with_marks_of_subject() {
        List<Student> result = studentRepository
                .findAllInClassWithMarksOfTheSubject("1a", "history");

        assertThat(result).extracting("id", "name", "surname")
                .containsAll(List.of(tuple(1L, "studentName1", "studentSurname1"),
                        tuple(2L, "studentName2", "studentSurname2")));
        assertThat(result.get(0).getMarks()).hasSize(1);
        assertThat(result.get(1).getMarks()).hasSize(3);
    }
}