package pl.schoolmanagementsystem.common.student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.schoolmanagementsystem.DataJpaTestBase;
import pl.schoolmanagementsystem.common.student.dto.StudentOutputDto2;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class StudentRepositoryTest extends DataJpaTestBase {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void should_return_all_students_in_school_class() {
        List<StudentOutputDto2> result = studentRepository.findAllInClass("1a");

        assertThat(result).extracting("studentId", "name", "surname")
                .containsAll(List.of(tuple(3, "name", "surname"),
                        tuple(4, "name", "surname")));
    }

    @Test
    void should_return_all_students_in_school_class_with_marks_of_subject() {
        List<Student> result = studentRepository
                .findAllInClassWithMarksOfTheSubject("1a", "history");

        assertThat(result).extracting("id", "name", "surname")
                .containsAll(List.of(tuple(3, "name", "surname"),
                        tuple(4, "name", "surname")));
        assertThat(result.get(0).getMarks()).hasSize(1);
        assertThat(result.get(1).getMarks()).hasSize(3);
    }
}