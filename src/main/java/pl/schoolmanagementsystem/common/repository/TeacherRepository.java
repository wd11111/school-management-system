package pl.schoolmanagementsystem.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.common.model.Teacher;
import pl.schoolmanagementsystem.teacher.dto.SubjectAndClassDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.teacherInClasses tic JOIN FETCH tic.taughtClasses WHERE t.id = ?1")
    Optional<Teacher> findByIdAndFetchClasses(Long id);

    @Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.taughtSubjects WHERE t.id = ?1")
    Optional<Teacher> findByIdAndFetchSubjects(Long id);

    @Query("SELECT DISTINCT t FROM Teacher t LEFT JOIN FETCH t.taughtSubjects ORDER BY t.surname")
    List<Teacher> findAllAndFetchSubjects();

    @Query("SELECT new pl.schoolmanagementsystem.teacher.dto.SubjectAndClassDto(" +
            "tc.taughtSubject, tics.name) " +
            "FROM TeacherInClass tc " +
            "LEFT JOIN tc.taughtClasses tics " +
            "WHERE tc.teacher.appUser.userEmail=?1 " +
            "ORDER BY tics.name")
    Page<SubjectAndClassDto> findTaughtClassesByTeacher(String email, Pageable pageable);
}
