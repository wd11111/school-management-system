package pl.schoolmanagementsystem.common.schoolClass;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.common.schoolClass.dto.SchoolClassDto;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, String> {

    @Query("SELECT new pl.schoolmanagementsystem.common.schoolClass.dto.SchoolClassDto(c.name) FROM SchoolClass c")
    Page<SchoolClassDto> findAllClasses(Pageable pageable);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM teacher_in_class_taught_classes where taught_classes_name=?1")
    void deleteTaughtClasses(String schoolClassName);
}
