package pl.schoolmanagementsystem.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.common.model.Teacher;
import pl.schoolmanagementsystem.common.model.TeacherInClass;

import java.util.Optional;

@Repository
public interface TeacherInClassRepository extends JpaRepository<TeacherInClass, Long> {

    Optional<TeacherInClass> findByTeacherAndTaughtSubject(Teacher teacher, String schoolSubject);

    boolean existsByTeacher_AppUser_UserEmailAndTaughtSubjectAndTaughtClasses_Name(String email, String taughtSubject, String className);
}
