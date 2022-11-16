package pl.schoolmanagementsystem.common.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.common.student.dto.StudentOutputDto2;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("select new pl.schoolmanagementsystem.student.dto.StudentOutputDto2(s.id, s.name, s.surname) " +
            "from Student s where s.schoolClass=?1 order by s.surname")
    List<StudentOutputDto2> findAllInClass(String schoolClassName);

    Optional<Student> findByEmail_Email(String email);

    @Query("select distinct s from Student s left join fetch s.marks m where s.schoolClass=?1 and " +
            "(m.subject=?2 or m.subject is null)")
    List<Student> findAllInClassWithMarksOfTheSubject(String schoolClass, String subject);

    @Query("select s.id from Student s where s.email.email=?1")
    int findIdByEmail(String email);

    boolean existsByEmail_Email(String email);
}
