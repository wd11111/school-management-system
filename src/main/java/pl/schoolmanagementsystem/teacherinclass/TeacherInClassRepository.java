package pl.schoolmanagementsystem.teacherinclass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubject;
import pl.schoolmanagementsystem.teacher.Teacher;

import java.util.Optional;

@Repository
public interface TeacherInClassRepository extends JpaRepository<TeacherInClass, Integer> {

    @Query("select t from TeacherInClass t left join fetch t.taughtClasses join fetch t.taughtSubject " +
            "join fetch t.teacher " +
            "where t.teacher=?1 and t.taughtSubject=?2")
    Optional<TeacherInClass> findByTeacherAndTaughtSubject(Teacher teacher, SchoolSubject schoolSubject);
}
