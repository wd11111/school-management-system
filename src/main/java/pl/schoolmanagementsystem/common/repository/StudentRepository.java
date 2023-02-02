package pl.schoolmanagementsystem.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.schoolClass.dto.StudentDto;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student>, QuerydslPredicateExecutor<Student> {

    @Query("SELECT new pl.schoolmanagementsystem.schoolClass.dto.StudentDto(s.id, s.name, s.surname) " +
            "FROM Student s WHERE s.schoolClass=?1 ORDER BY s.surname")
    List<StudentDto> findAllInClass(String schoolClassName);

    @Query("SELECT s.schoolClass from Student s where s.appUser.userEmail=?1")
    String findStudentsClass(String studentEmail);

}
