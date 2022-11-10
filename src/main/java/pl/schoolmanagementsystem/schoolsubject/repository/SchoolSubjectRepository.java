package pl.schoolmanagementsystem.schoolsubject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndTeacherOutputDto;
import pl.schoolmanagementsystem.schoolsubject.model.SchoolSubject;
import pl.schoolmanagementsystem.schoolsubject.dto.SchoolSubjectDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolSubjectRepository extends JpaRepository<SchoolSubject, String> {

    @Query("select s from SchoolSubject s left join fetch s.teachersInClasses where s.name=?1")
    Optional<SchoolSubject> findBySubjectName(String subjectName);

    boolean existsByName(String name);

    @Query("select new pl.schoolmanagementsystem.schoolsubject.dto.SchoolSubjectDto(s.name) from SchoolSubject s")
    List<SchoolSubjectDto> findAllSchoolSubjects();

    @Modifying
    @Query(nativeQuery = true, value = "delete from teacher_taught_subjects where taught_subjects_name=?1")
    void deleteTaughtSubjects(String subjectName);

    @Query("select new pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndTeacherOutputDto(" +
            "t.taughtSubject.name, t.teacher.name, t.teacher.surname) " +
            "from SchoolClass c join c.teachersInClass t where c.name=?1 " +
            "order by t.taughtSubject.name")
    List<SubjectAndTeacherOutputDto> findAllSubjectsForSchoolClass(String className);

}
