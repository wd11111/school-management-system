package pl.schoolmanagementsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.Model.SchoolSubject;
import pl.schoolmanagementsystem.Model.Teacher;
import pl.schoolmanagementsystem.Model.TeacherInClass;

import java.util.Optional;

@Repository
public interface TeacherInClassRepository extends JpaRepository<TeacherInClass, Integer> {

    Optional<TeacherInClass> findByTeacherAndTaughtSubject(Teacher teacher, SchoolSubject schoolSubject);
}
