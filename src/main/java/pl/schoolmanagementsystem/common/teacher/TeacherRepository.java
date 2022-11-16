package pl.schoolmanagementsystem.common.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndClassDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Override
    @Query("select t from Teacher t left join fetch t.teacherInClasses left join fetch t.taughtSubjects " +
            "where t.id = ?1")
    Optional<Teacher> findById(Integer id);

    @Override
    @Query("select distinct t from Teacher t left join fetch t.taughtSubjects order by t.surname")
    List<Teacher> findAll();

    @Query("select t.appUser.userEmail from Teacher t where t.id=?1")
    String findEmailById(int id);

    @Query("select new pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndClassDto(" +
            "tc.taughtSubject, tics.name) " +
            "from TeacherInClass tc " +
            "left join tc.taughtClasses tics " +
            "where tc.teacher.appUser.userEmail=?1")
    List<SubjectAndClassDto> findTaughtClassesByTeacher(String email);
}
