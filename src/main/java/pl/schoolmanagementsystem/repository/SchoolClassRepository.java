package pl.schoolmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.model.dto.SchoolClassDto;
import pl.schoolmanagementsystem.model.dto.output.SubjectAndTeacherOutputDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, String> {

    @Query("select c from SchoolClass c left join fetch c.teachersInClass " +
            "left join fetch c.students where c.schoolClassName=?1")
    Optional<SchoolClass> findBySchoolClassName(String schoolClassName);

    boolean existsBySchoolClassName(String schoolClassName);

    @Query("select new pl.schoolmanagementsystem.model.dto.SchoolClassDto(c.schoolClassName) from SchoolClass c")
    List<SchoolClassDto> findAllSchoolClasses();

    @Query("select new pl.schoolmanagementsystem.model.dto.output.SubjectAndTeacherOutputDto(" +
            "t.taughtSubject.subjectName, t.teacher.name, t.teacher.surname) " +
            "from SchoolClass c join c.teachersInClass t where c.schoolClassName=?1 " +
            "order by t.taughtSubject.subjectName")
    List<SubjectAndTeacherOutputDto> findAllSubjectsForSchoolClass(String className);

}
