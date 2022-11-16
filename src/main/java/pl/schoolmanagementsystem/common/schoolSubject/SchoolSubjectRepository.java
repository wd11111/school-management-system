package pl.schoolmanagementsystem.common.schoolSubject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndTeacherOutputDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolSubjectRepository extends JpaRepository<SchoolSubject, String> {

    Optional<SchoolSubject> findByNameIgnoreCase(String subjectName);

    boolean existsByName(String name);

    @Query("select new pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto(s.name) from SchoolSubject s")
    List<SchoolSubjectDto> findAllSchoolSubjects();

    @Modifying
    @Query(nativeQuery = true, value = "delete from teacher_taught_subjects where taught_subjects_name=?1")
    void deleteTaughtSubjects(String subjectName);

    @Query("select new pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndTeacherOutputDto(" +
            "t.taughtSubject, t.teacher.name, t.teacher.surname) " +
            "from SchoolClass c join c.teachersInClass t where c.name=?1 " +
            "order by t.taughtSubject")
    List<SubjectAndTeacherOutputDto> findAllSubjectsInSchoolClass(String className);
}
