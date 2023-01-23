package pl.schoolmanagementsystem.common.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.schoolmanagementsystem.BaseContainerTest;
import pl.schoolmanagementsystem.common.dto.StudentDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class StudentRepositoryTest extends BaseContainerTest {

    public static final String STUDENTS_EMAIL = "email3";

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void should_return_all_students_in_school_class() {
        List<StudentDto> result = studentRepository.findAllInClass("1a");

        assertThat(result).extracting("studentId", "name", "surname")
                .containsAll(List.of(
                        tuple(1L, "studentName1", "studentSurname1"),
                        tuple(2L, "studentName2", "studentSurname2")));
    }

    @Test
    void should_return_students_class() {
        String studentsClass = studentRepository.findStudentsClass(STUDENTS_EMAIL);

        assertThat(studentsClass).isEqualTo("1a");
    }
}