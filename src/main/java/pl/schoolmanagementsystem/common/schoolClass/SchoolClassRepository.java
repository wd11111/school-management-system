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

    @Query("select new pl.schoolmanagementsystem.common.schoolClass.dto.SchoolClassDto(c.name) from SchoolClass c")
    Page<SchoolClassDto> findAllClasses(Pageable pageable);

    @Modifying
    @Query(nativeQuery = true, value = "delete from teacher_in_class_taught_classes where taught_classes_name=?1")
    void deleteTaughtClasses(String schoolClassName);
}
