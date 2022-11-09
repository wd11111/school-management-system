package pl.schoolmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.model.SchoolSubject;
import pl.schoolmanagementsystem.model.dto.SchoolSubjectDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolSubjectRepository extends JpaRepository<SchoolSubject, String> {

    @Query("select s from SchoolSubject s left join fetch s.teachersInClasses where s.name=?1")
    Optional<SchoolSubject> findBySubjectName(String subjectName);

    boolean existsByName(String name);

    @Query("select new pl.schoolmanagementsystem.model.dto.SchoolSubjectDto(s.name) from SchoolSubject s")
    List<SchoolSubjectDto> findAllSchoolSubjects();

    @Modifying
    @Query(nativeQuery = true, value = "delete from teacher_taught_subjects where taught_subjects_name=?1")
    void deleteTaughtSubjects(String subjectName);

}
