package pl.schoolmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.model.SchoolSubject;

import java.util.Optional;

@Repository
public interface SchoolSubjectRepository extends JpaRepository<SchoolSubject, String> {

    @Query("select s from SchoolSubject s left join fetch s.teachersInClasses where s.subjectName=?1")
    Optional<SchoolSubject> findBySubjectName(String subjectName);

    boolean existsBySubjectName(String name);

}
