package pl.schoolmanagementsystem.common.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.schoolmanagementsystem.BaseContainerTest;
import pl.schoolmanagementsystem.schoolClass.dto.StudentDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class StudentRepositoryTest extends BaseContainerTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void should_return_all_students_in_school_class() {
        List<StudentDto> result = studentRepository.findAllInClass("1A");

        assertThat(result).extracting("name", "surname")
                .containsAll(List.of(
                        tuple("Jan", "Kowalski"),
                        tuple("Joanna", "Nowak"),
                        tuple("Tomasz", "Wiśniewski"),
                        tuple("Anna", "Wójcik"),
                        tuple("Piotr", "Kowalczyk")));
    }

    @Test
    void should_return_students_class() {
        String studentsClass = studentRepository.findStudentsClass("joanna.nowak.2@example.com");

        assertThat(studentsClass).isEqualTo("1A");
    }
}