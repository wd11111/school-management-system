package pl.schoolmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.model.Mark;
import pl.schoolmanagementsystem.model.Student;
import pl.schoolmanagementsystem.model.dto.MarkAvgDto;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("select new pl.schoolmanagementsystem.model.dto.MarkAvgDto(m.subject, avg(m.mark)) from Student s" +
            " left join Mark m on s.studentId = m.student.studentId where m.student.studentId=?1 group by m.subject")
    List<MarkAvgDto> findAllAverageMarksForStudentById(int id);

    @Query("select s.marks from Student s where s.studentId=?1")
    List<Mark> findAllMarksForStudentById(int id);
}
