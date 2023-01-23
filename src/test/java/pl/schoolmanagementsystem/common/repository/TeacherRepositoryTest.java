package pl.schoolmanagementsystem.common.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.schoolmanagementsystem.BaseContainerTest;
import pl.schoolmanagementsystem.common.model.Teacher;
import pl.schoolmanagementsystem.teacher.dto.SubjectAndClassDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class TeacherRepositoryTest extends BaseContainerTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    void should_find_teacher_by_id() {
        Teacher result = teacherRepository.findByIdAndFetchSubjects(1L).get();

        assertThat(result).extracting("id", "name", "surname")
                .containsAll(List.of(1L, "teacherName1", "teacherSurname1"));
    }

    @Test
    void should_return_all_teachers() {
        List<Teacher> result = teacherRepository.findAllAndFetchSubjects();

        assertThat(result).extracting("surname")
                .containsExactly(
                        "teacherSurname1",
                        "teacherSurname2");
    }

    @Test
    void should_return_taught_classes_by_teacher() {
        Pageable pageable = PageRequest.of(0, 5);

        Page<SubjectAndClassDto> result = teacherRepository.findTaughtClassesByTeacher("email2", pageable);

        assertThat(result).extracting("schoolSubject", "schoolClass")
                .containsAll(List.of(
                        tuple("biology", "1a"),
                        tuple("history", "1a")));
    }
}