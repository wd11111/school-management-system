package pl.schoolmanagementsystem.mark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.mark.dto.MarkDtoWithTwoFields;
import pl.schoolmanagementsystem.mark.model.Mark;

import java.util.List;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Integer> {

    @Query("select new pl.schoolmanagementsystem.mark.dto.MarkAvgDto(m.subject.name, avg(m.mark)) " +
            "from Student s left join Mark m on s.id = m.student.id " +
            "where m.student.id=?1 group by m.subject")
    List<MarkAvgDto> findAllAverageMarksForStudentById(int id);

    @Query("select new pl.schoolmanagementsystem.mark.dto.MarkDtoWithTwoFields(m.mark, m.subject.name) " +
            "from Student s left join Mark m on m.student=s where s.id=?1")
    List<MarkDtoWithTwoFields> findAllMarksForStudentById(int id);
}
