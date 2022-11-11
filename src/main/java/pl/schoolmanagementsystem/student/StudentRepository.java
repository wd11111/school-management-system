package pl.schoolmanagementsystem.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto2;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("select new pl.schoolmanagementsystem.student.dto.StudentOutputDto2(s.id, s.name, s.surname) " +
            "from Student s where s.schoolClass.name=?1 order by s.surname")
    List<StudentOutputDto2> findAllStudentsInClass(String schoolClassName);

    Optional<Student> findByEmail_Email(String email);

    @Query("select distinct s from Student s join fetch s.marks m where s.schoolClass.name=?1 and m.subject.name=?2")
    List<Student> findAllStudentsInClassWithMarksOfTheSubject(String schoolClass, String subject);
}
