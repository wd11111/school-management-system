package pl.schoolmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.model.Student;
import pl.schoolmanagementsystem.model.dto.MarkAvgDto;
import pl.schoolmanagementsystem.model.dto.MarkDtoWithTwoFields;
import pl.schoolmanagementsystem.model.dto.output.StudentOutputDto2;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("select new pl.schoolmanagementsystem.model.dto.MarkAvgDto(m.subject.subjectName, avg(m.mark)) " +
            "from Student s left join Mark m on s.studentId = m.student.studentId " +
            "where m.student.studentId=?1 group by m.subject")
    List<MarkAvgDto> findAllAverageMarksForStudentById(int id);

    @Query("select new pl.schoolmanagementsystem.model.dto.MarkDtoWithTwoFields(m.mark, m.subject.subjectName) " +
            "from Student s left join Mark m on m.student=s where s.studentId=?1")
    List<MarkDtoWithTwoFields> findAllMarksForStudentById(int id);

    @Query("select new pl.schoolmanagementsystem.model.dto.output.StudentOutputDto2(s.studentId, s.name, s.surname) from Student s where s.schoolClass.schoolClassName=?1 order by s.surname")
    List<StudentOutputDto2> findAllStudentsInClass(String schoolClassName);
}
