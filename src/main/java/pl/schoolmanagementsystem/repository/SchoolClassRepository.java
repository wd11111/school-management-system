package pl.schoolmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.model.SchoolClass;

import java.util.Optional;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, String> {

    @Query("select c from SchoolClass c left join fetch c.teachersInClass " +
            "left join fetch c.students where c.schoolClassName=?1")
    Optional<SchoolClass> findBySchoolClassName(String schoolClassName);

    boolean existsBySchoolClassName(String schoolClassName);
}
