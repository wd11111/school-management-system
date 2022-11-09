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
            "left join fetch c.students where c.name=?1")
    Optional<SchoolClass> findBySchoolClassName(String schoolClassName);

    boolean existsByName(String schoolClassName);

    @Query("select new pl.schoolmanagementsystem.model.dto.SchoolClassDto(c.name) from SchoolClass c")
    List<SchoolClassDto> findAllSchoolClasses();

    @Query("select new pl.schoolmanagementsystem.model.dto.output.SubjectAndTeacherOutputDto(" +
            "t.taughtSubject.name, t.teacher.name, t.teacher.surname) " +
            "from SchoolClass c join c.teachersInClass t where c.name=?1 " +
            "order by t.taughtSubject.name")
    List<SubjectAndTeacherOutputDto> findAllSubjectsForSchoolClass(String className);
}
