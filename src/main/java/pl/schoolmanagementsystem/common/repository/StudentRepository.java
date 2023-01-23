package pl.schoolmanagementsystem.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.common.dto.StudentDto;
import pl.schoolmanagementsystem.common.model.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    @Query("SELECT new pl.schoolmanagementsystem.common.dto.StudentDto(s.id, s.name, s.surname) " +
            "FROM Student s WHERE s.schoolClass=?1 ORDER BY s.surname")
    List<StudentDto> findAllInClass(String schoolClassName);

    @Query("SELECT s.schoolClass from Student s where s.appUser.userEmail=?1")
    String findStudentsClass(String studentEmail);

}
