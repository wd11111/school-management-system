package pl.schoolmanagementsystem.mark;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.mark.dto.MarkDtoWithTwoFields;
import pl.schoolmanagementsystem.mark.dto.MarkInputDto;
import pl.schoolmanagementsystem.mark.dto.MarkOutputDto;
import pl.schoolmanagementsystem.mark.uitls.MarkCreator;
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
public class MarkFacade {

    private final MarkService markService;

    private final StudentService studentService;

    private final TeacherService teacherService;

    private final TeacherInClassService teacherInClassService;

    private final SchoolSubjectService schoolSubjectService;

    private final MarkMapper markMapper;

    public MarkOutputDto addMarkForStudent(String teacherEmail, MarkInputDto markInputDto, int studentId) {
        Student student = studentService.findById(studentId);
        SchoolClass studentsClass = student.getSchoolClass();
        Teacher teacher = teacherService.findByEmailOrThrow(teacherEmail);
        SchoolSubject schoolSubject = schoolSubjectService.findByNameOrThrow(markInputDto.getSubject());
        teacherInClassService.makeSureTeacherTeachesThisSubjectInClass(teacher, schoolSubject, studentsClass);
        Mark mark = MarkCreator.create(markInputDto.getMark(), student, schoolSubject);
        markService.save(mark);
        return markMapper.mapMarkToOutputDto(mark);
    }

    public Map<String, List<Integer>> getGroupedMarksBySubjectForStudent(int studentId) {
        studentService.checkIfStudentExists(studentId);
        List<MarkDtoWithTwoFields> studentsMarks = markService.findAllMarksForStudent(studentId);
        Map<String, List<MarkDtoWithTwoFields>> groupedMarks = markService.groupMarksBySubject(studentsMarks);
        return markMapper.mapListOfMarksToIntegersInMapStructure(groupedMarks);
    }

    public List<MarkAvgDto> getAverageMarksForStudent(int studentId) {
        studentService.checkIfStudentExists(studentId);
        return markService.findAllAverageMarksForStudentById(studentId);
    }
}
