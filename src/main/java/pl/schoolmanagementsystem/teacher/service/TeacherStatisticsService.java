package pl.schoolmanagementsystem.teacher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.schoolmanagementsystem.common.exception.TeacherDoesNotTeachSubjectException;
import pl.schoolmanagementsystem.common.repository.MarkRepository;
import pl.schoolmanagementsystem.common.repository.TeacherRepository;
import pl.schoolmanagementsystem.teacher.dto.MarkStatisticsDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TeacherStatisticsService {

    private final TeacherRepository teacherRepository;

    private final MarkRepository markRepository;

    public List<MarkStatisticsDto> getStatistics(String teacherEmail, String subject) {
        validateTeacherTeachesSubject(subject);

        List<String> taughtClassesByTeacher = teacherRepository.findTaughtClassesNamesByTeacher(teacherEmail, subject);
        return markRepository.findStatisticsOfSubjectForEachClass(taughtClassesByTeacher, subject);

    }

    private void validateTeacherTeachesSubject(String subject) {
        if (!teacherRepository.existsByTaughtSubjects_Name(subject)) {
            throw new TeacherDoesNotTeachSubjectException(subject);
        }
    }


}
