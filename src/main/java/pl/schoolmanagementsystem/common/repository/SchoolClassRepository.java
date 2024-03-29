package pl.schoolmanagementsystem.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.schoolClass.dto.SchoolClassDto;

import java.util.Optional;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, String> {

    @Query("SELECT sc FROM SchoolClass sc LEFT JOIN FETCH sc.students s LEFT JOIN s.marks m WHERE sc.name=?1")
    Optional<SchoolClass> findClassAndFetchStudentsWithMarks(String schoolClass);

    @Query("SELECT new pl.schoolmanagementsystem.schoolClass.dto.SchoolClassDto(c.name) FROM SchoolClass c")
    Page<SchoolClassDto> findAllClasses(Pageable pageable);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM teacher_in_class_taught_classes where taught_classes_name=?1")
    void deleteTaughtClasses(String schoolClassName);
}
