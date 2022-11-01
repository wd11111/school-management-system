package pl.schoolmanagementsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.Model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
}
