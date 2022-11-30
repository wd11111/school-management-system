package pl.schoolmanagementsystem.common.mark;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.common.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.common.mark.dto.MarkWithTwoFields;

import java.util.List;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {

    @Query("select new pl.schoolmanagementsystem.common.mark.dto.MarkAvgDto(m.subject, avg(m.mark)) " +
            "from Student s left join Mark m on s.id = m.studentId " +
            "where m.studentId=?1 group by m.subject")
    List<MarkAvgDto> findAllAveragesForStudent(long studentId);

    @Query("select new pl.schoolmanagementsystem.common.mark.dto.MarkWithTwoFields(m.mark, m.subject) " +
            "from Student s left join s.marks m where s.appUser.userEmail=?1")
    List<MarkWithTwoFields> findAllMarksForStudent(String studentEmail);
}
