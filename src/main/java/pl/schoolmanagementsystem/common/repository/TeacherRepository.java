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
            "tic.taughtSubject, c.name) " +
            "FROM TeacherInClass tic " +
            "LEFT JOIN tic.taughtClasses c " +
            "WHERE tic.teacher.appUser.email=?1 " +
            "ORDER BY c.name")
    Page<SubjectAndClassDto> findTaughtClassesByTeacher(String teacherEmail, Pageable pageable);

    @Query("SELECT c.name " +
            "FROM TeacherInClass tic " +
            "LEFT JOIN tic.taughtClasses c " +
            "WHERE tic.teacher.appUser.email=?1 " +
            "AND tic.taughtSubject=?2 " +
            "ORDER BY c.name")
    List<String> findTaughtClassesNamesByTeacher(String teacherEmail, String subject);

    boolean existsByTaughtSubjects_Name(String subject);
}
