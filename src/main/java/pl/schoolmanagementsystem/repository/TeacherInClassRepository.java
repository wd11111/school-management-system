package pl.schoolmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.model.SchoolSubject;
import pl.schoolmanagementsystem.model.Teacher;
import pl.schoolmanagementsystem.model.TeacherInClass;
import pl.schoolmanagementsystem.model.dto.output.SubjectAndClassOutputDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherInClassRepository extends JpaRepository<TeacherInClass, Integer> {

    @Query("select t from TeacherInClass t left join fetch t.taughtClasses join fetch t.taughtSubject " +
            "join fetch t.teacher " +
            "where t.teacher=?1 and t.taughtSubject=?2")
    Optional<TeacherInClass> findByTeacherAndTaughtSubject(Teacher teacher, SchoolSubject schoolSubject);

    @Query("select new pl.schoolmanagementsystem.model.dto.output.SubjectAndClassOutputDto(" +
            "tc.taughtSubject.subjectName, tics.schoolClassName) " +
            "from TeacherInClass tc " +
            "left join tc.taughtClasses tics " +
            "where tc.teacher.teacherId=?1")
    List<SubjectAndClassOutputDto> findTaughtClassesByTeacher(int teacherId);


}
