package pl.schoolmanagementsystem.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.common.model.Mark;
import pl.schoolmanagementsystem.student.dto.MarkAvgDto;
import pl.schoolmanagementsystem.student.dto.MarkDto;
import pl.schoolmanagementsystem.teacher.dto.MarkStatisticsDto;

import java.util.List;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {

    @Query("SELECT new pl.schoolmanagementsystem.student.dto.MarkAvgDto(m.subject, AVG(m.mark)) " +
            "FROM Student s LEFT JOIN Mark m ON s.id = m.studentId " +
            "WHERE s.appUser.email=?1 GROUP BY m.subject")
    List<MarkAvgDto> findAllAveragesForStudent(String studentEmail);

    @Query("SELECT new pl.schoolmanagementsystem.student.dto.MarkDto(m.mark, m.subject) " +
            "FROM Student s LEFT JOIN s.marks m WHERE s.appUser.email=?1")
    List<MarkDto> findAllMarksForStudent(String studentEmail);

    @Query("select new pl.schoolmanagementsystem.teacher.dto.MarkStatisticsDto(s.schoolClass, avg(m.mark)) " +
            "from Mark m join Student s on m.studentId=s.id " +
            "where s.schoolClass in (?1) and m.subject=?2 " +
            "group by s.schoolClass")
    List<MarkStatisticsDto> findStatisticsOfSubjectForEachClass(List<String> classes, String subject);

}
