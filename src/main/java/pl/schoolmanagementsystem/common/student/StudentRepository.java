package pl.schoolmanagementsystem.common.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.common.student.dto.StudentDto;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT new pl.schoolmanagementsystem.common.student.dto.StudentDto(s.id, s.name, s.surname) " +
            "FROM Student s WHERE s.schoolClass=?1 ORDER BY s.surname")
    List<StudentDto> findAllInClass(String schoolClassName);

    @Query("SELECT s.schoolClass from Student s where s.appUser.userEmail=?1")
    String findStudentClass(String studentEmail);

}
