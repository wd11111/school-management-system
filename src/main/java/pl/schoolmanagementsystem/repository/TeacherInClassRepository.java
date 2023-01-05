package pl.schoolmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.model.Teacher;
import pl.schoolmanagementsystem.model.TeacherInClass;

import java.util.Optional;

@Repository
public interface TeacherInClassRepository extends JpaRepository<TeacherInClass, Long> {

    @Query("SELECT t FROM TeacherInClass t LEFT JOIN FETCH t.taughtClasses " +
            "WHERE t.teacher=?1 AND t.taughtSubject=?2")
    Optional<TeacherInClass> findByTeacherAndTaughtSubject(Teacher teacher, String schoolSubject);

    boolean existsByTeacher_AppUser_UserEmailAndTaughtSubjectAndTaughtClasses_Name(String email, String taughtSubject, String className);
}
