package pl.schoolmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.model.Mark;

import java.util.List;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Integer> {

    @Query("select new pl.schoolmanagementsystem.model.Mark(0, m.mark, m.student, m.subject)" +
            " from Mark m where m.student = ?1")
    List<Mark> findAllMarksForStudentById(int id);

}


