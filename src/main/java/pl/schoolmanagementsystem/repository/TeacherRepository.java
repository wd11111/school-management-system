package pl.schoolmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.model.Teacher;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Override
    @Query("select t from Teacher t left join fetch t.taughtSubjects" +
            " left join fetch t.teacherInClasses where t.teacherId = ?1")
    Optional<Teacher> findById(Integer integer);

}
