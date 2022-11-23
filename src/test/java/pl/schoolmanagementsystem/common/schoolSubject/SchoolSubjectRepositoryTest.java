package pl.schoolmanagementsystem.common.schoolSubject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.schoolmanagementsystem.DataJpaTestBase;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndTeacherOutputDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class SchoolSubjectRepositoryTest extends DataJpaTestBase {

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
        List<SubjectAndTeacherOutputDto> result = schoolSubjectRepository.findAllSubjectsInSchoolClass("1a");

        assertThat(result).extracting("subject", "teacherName", "teacherSurname")
                .containsAll(List.of(tuple("biology", "name", "surname"),
                        tuple("history", "name", "surname")));
    }
}