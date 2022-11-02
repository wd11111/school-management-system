package pl.schoolmanagementsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.Model.SchoolClass;

import java.util.Optional;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, String> {

    Optional<SchoolClass> findBySchoolClassName(String schoolClassName);

    boolean existsBySchoolClassName(String schoolClassName);
}
