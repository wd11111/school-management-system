package pl.schoolmanagementsystem.mark;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.mark.dto.MarkDtoWithTwoFields;
import pl.schoolmanagementsystem.mark.dto.MarkInputDto;
import pl.schoolmanagementsystem.mark.dto.MarkOutputDto;
import pl.schoolmanagementsystem.mark.uitls.MarkBuilder;
import pl.schoolmanagementsystem.mark.uitls.MarkMapper;
import pl.schoolmanagementsystem.schoolclass.SchoolClass;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubject;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubjectService;
import pl.schoolmanagementsystem.student.Student;
import pl.schoolmanagementsystem.student.StudentService;
import pl.schoolmanagementsystem.teacher.Teacher;
import pl.schoolmanagementsystem.teacher.TeacherService;
import pl.schoolmanagementsystem.teacherinclass.TeacherInClassService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MarkService {

    private final MarkRepository markRepository;

    private final StudentService studentService;

    private final TeacherService teacherService;

    private final TeacherInClassService teacherInClassService;

    private final SchoolSubjectService schoolSubjectService;

    public MarkOutputDto addMarkForStudent(String teacherEmail, MarkInputDto markInputDto, int studentId) {
        Student student = studentService.findById(studentId);
        SchoolClass studentsClass = student.getSchoolClass();
        Teacher teacher = teacherService.findByEmail(teacherEmail);
        SchoolSubject schoolSubject = schoolSubjectService.findByName(markInputDto.getSubject());
        teacherInClassService.makeSureIfTeacherTeachesThisClass(teacher, schoolSubject, studentsClass);
        Mark mark = markRepository.save(MarkBuilder.build(markInputDto.getMark(), student, schoolSubject));
        return MarkMapper.mapMarkToOutputDto(mark);
    }

    public Map<String, List<Integer>> getGroupedMarksBySubjectForStudent(int studentId) {
        studentService.checkIfStudentExists(studentId);
        return groupMarksForStudent(studentId);
    }

    public List<MarkAvgDto> getAverageMarksForStudent(int studentId) {
        studentService.checkIfStudentExists(studentId);
        return markRepository.findAllAverageMarksForStudentById(studentId);
    }

    private Map<String, List<Integer>> groupMarksForStudent(int studentId) {
        List<MarkDtoWithTwoFields> studentsMarks = markRepository.findAllMarksForStudentById(studentId);
        Map<String, List<MarkDtoWithTwoFields>> groupedMarks = MarkMapper.groupMarksBySubject(studentsMarks);
        return MarkMapper.mapListOfMarksToIntegersInMapStructure(groupedMarks);
    }
}
