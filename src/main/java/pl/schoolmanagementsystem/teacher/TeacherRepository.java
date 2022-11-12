package pl.schoolmanagementsystem.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Override
    @Query("select t from Teacher t left join fetch t.teacherInClasses left join fetch t.taughtSubjects " +
            "where t.id = ?1")
    Optional<Teacher> findById(Integer id);

    @Override
    @Query("select distinct t from Teacher t join fetch t.taughtSubjects order by t.surname")
    List<Teacher> findAll();

    Optional<Teacher> findByEmail_Email(String email);

    @Query("select t.id from Teacher t where t.email.email=?1")
    int findIdByEmail(String email);

    @Query("select new pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto(" +
            "tc.taughtSubject.name, tics.name) " +
            "from TeacherInClass tc " +
            "left join tc.taughtClasses tics " +
            "where tc.teacher.id=?1")
    List<SubjectAndClassOutputDto> findTaughtClassesByTeacher(int teacherId);
}
