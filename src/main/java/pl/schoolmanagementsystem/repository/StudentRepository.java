package pl.schoolmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
}
