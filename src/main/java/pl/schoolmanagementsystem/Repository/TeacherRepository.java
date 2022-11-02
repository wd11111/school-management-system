package pl.schoolmanagementsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.Model.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

}
