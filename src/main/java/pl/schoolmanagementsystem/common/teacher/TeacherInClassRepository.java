package pl.schoolmanagementsystem.common.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherInClassRepository extends JpaRepository<TeacherInClass, Integer> {

    @Query("select t from TeacherInClass t left join fetch t.taughtClasses where t.teacher=?1 and t.taughtSubject=?2")
    Optional<TeacherInClass> findByTeacherAndTaughtSubject(Teacher teacher, String schoolSubject);

    boolean existsByTeacher_Email_EmailAndTaughtSubjectAndTaughtClasses_Name(String email, String taughtSubject, String className);
}