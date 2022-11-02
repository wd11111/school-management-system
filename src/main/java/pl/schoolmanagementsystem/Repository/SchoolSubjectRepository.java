package pl.schoolmanagementsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.Model.SchoolSubject;

import java.util.Optional;

@Repository
public interface SchoolSubjectRepository extends JpaRepository<SchoolSubject, String> {

    Optional<SchoolSubject> findBySubjectName(String name);
    boolean existsBySubjectName(String name);
}
