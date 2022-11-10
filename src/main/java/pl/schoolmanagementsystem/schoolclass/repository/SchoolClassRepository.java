package pl.schoolmanagementsystem.schoolclass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.schoolclass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.schoolclass.model.SchoolClass;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, String> {

    @Query("select c from SchoolClass c left join fetch c.teachersInClass " +
            "left join fetch c.students where c.name=?1")
    Optional<SchoolClass> findBySchoolClassName(String schoolClassName);

    boolean existsByName(String schoolClassName);

    @Query("select new pl.schoolmanagementsystem.schoolclass.dto.SchoolClassDto(c.name) from SchoolClass c")
    List<SchoolClassDto> findAllSchoolClasses();
}
