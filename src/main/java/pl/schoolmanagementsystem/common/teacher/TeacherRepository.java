package pl.schoolmanagementsystem.common.teacher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndClassDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.teacherInClasses LEFT JOIN FETCH t.taughtSubjects " +
            "WHERE t.id = ?1")
    Optional<Teacher> findById(Long id);

    @Query("SELECT DISTINCT t FROM Teacher t LEFT JOIN FETCH t.taughtSubjects ORDER BY t.surname")
    List<Teacher> findAllAndFetchSubjects();

    @Query("SELECT t.appUser.userEmail FROM Teacher t WHERE t.id=?1")
    String findEmailById(long id);

    @Query("SELECT new pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndClassDto(" +
            "tc.taughtSubject, tics.name) " +
            "FROM TeacherInClass tc " +
            "LEFT JOIN tc.taughtClasses tics " +
            "WHERE tc.teacher.appUser.userEmail=?1 " +
            "ORDER BY tics.name")
    Page<SubjectAndClassDto> findTaughtClassesByTeacher(String email, Pageable pageable);
}
