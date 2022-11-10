package pl.schoolmanagementsystem.teacherinclass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.schoolsubject.model.SchoolSubject;
import pl.schoolmanagementsystem.teacher.model.Teacher;
import pl.schoolmanagementsystem.teacherinclass.model.TeacherInClass;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherInClassRepository extends JpaRepository<TeacherInClass, Integer> {

    @Query("select t from TeacherInClass t left join fetch t.taughtClasses join fetch t.taughtSubject " +
            "join fetch t.teacher " +
            "where t.teacher=?1 and t.taughtSubject=?2")
    Optional<TeacherInClass> findByTeacherAndTaughtSubject(Teacher teacher, SchoolSubject schoolSubject);

    @Query("select new pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto(" +
            "tc.taughtSubject.name, tics.name) " +
            "from TeacherInClass tc " +
            "left join tc.taughtClasses tics " +
            "where tc.teacher.id=?1")
    List<SubjectAndClassOutputDto> findTaughtClassesByTeacher(int teacherId);


}
