package pl.schoolmanagementsystem.Mark;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Integer> {

    @Query(nativeQuery = true, value = "SELECT subject, avg(mark),  as avg FROM" +
            "(select mark, subject from mark where student_id = ?1) group by subject")
    List<MarkAvg> findAllAverageMarksForStudentById(int id);

    @Query("select new pl.schoolmanagementsystem.Mark.Mark(0, m.mark, m.studentId, m.subject)" +
            " from Mark m where m.studentId = ?1")
    List<Mark> findAllMarksForStudentById(int id);

    public static interface MarkAvg {

        String getSubject();

        Double getAvg();
    }
}


