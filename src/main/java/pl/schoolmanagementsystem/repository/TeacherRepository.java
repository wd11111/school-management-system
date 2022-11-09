package pl.schoolmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.model.Teacher;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Override
    @Query("select t from Teacher t left join fetch t.teacherInClasses left join fetch t.taughtSubjects " +
            "where t.id = ?1")
    Optional<Teacher> findById(Integer id);

    @Override
    @Query("select distinct t from Teacher t join fetch t.taughtSubjects")
    List<Teacher> findAll();

    Optional<Teacher> findByEmail_Email(String email);
}
