package pl.schoolmanagementsystem.common.schoolSubject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.TaughtSubjectDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SchoolSubjectRepository extends JpaRepository<SchoolSubject, String> {

    Optional<SchoolSubject> findByNameIgnoreCase(String name);

    Set<SchoolSubject> findAllByNameIn(Set<String> names);

    @Query("SELECT new pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto(s.name) FROM SchoolSubject s")
    Page<SchoolSubjectDto> findAllSchoolSubjects(Pageable pageable);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM teacher_taught_subjects WHERE taught_subjects_name=?1")
    void deleteTaughtSubjects(String name);

    @Query("SELECT new pl.schoolmanagementsystem.common.schoolSubject.dto.TaughtSubjectDto(" +
            "t.taughtSubject, t.teacher.name, t.teacher.surname) " +
            "FROM SchoolClass c JOIN c.teachersInClass t WHERE c.name=?1 " +
            "ORDER BY t.taughtSubject")
    List<TaughtSubjectDto> findTaughtSubjectsInClass(String className);
}
