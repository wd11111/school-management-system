package pl.schoolmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.model.Student;
import pl.schoolmanagementsystem.model.dto.StudentResponseDto2;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT new pl.schoolmanagementsystem.common.student.dto.StudentResponseDto2(s.id, s.name, s.surname) " +
            "FROM Student s WHERE s.schoolClass=?1 ORDER BY s.surname")
    List<StudentResponseDto2> findAllInClass(String schoolClassName);

    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.marks m WHERE s.schoolClass=?1 AND " +
            "(m.subject=?2 OR m.subject IS NULL)")
    List<Student> findAllInClassWithMarksOfTheSubject(String schoolClass, String subject);

    @Query("SELECT s.id FROM Student s WHERE s.appUser.userEmail=?1")
    long findIdByEmail(String email);

}
