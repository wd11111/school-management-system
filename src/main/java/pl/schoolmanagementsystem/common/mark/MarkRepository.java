package pl.schoolmanagementsystem.common.mark;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.common.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.common.mark.dto.MarkDto;

import java.util.List;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {

    @Query("SELECT new pl.schoolmanagementsystem.common.mark.dto.MarkAvgDto(m.subject, AVG(m.mark)) " +
            "FROM Student s LEFT JOIN Mark m ON s.id = m.studentId " +
            "WHERE s.appUser.userEmail=?1 GROUP BY m.subject")
    List<MarkAvgDto> findAllAveragesForStudent(String studentEmail);

    @Query("SELECT new pl.schoolmanagementsystem.common.mark.dto.MarkDto(m.mark, m.subject) " +
            "FROM Student s LEFT JOIN s.marks m WHERE s.appUser.userEmail=?1")
    List<MarkDto> findAllMarksForStudent(String studentEmail);
}
