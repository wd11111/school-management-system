package pl.schoolmanagementsystem.mark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.mark.dto.MarkDtoWithTwoFields;
import pl.schoolmanagementsystem.mark.uitls.MarkBuilder;
import pl.schoolmanagementsystem.mark.uitls.MarkMapper;
import pl.schoolmanagementsystem.mark.dto.MarkInputDto;
import pl.schoolmanagementsystem.mark.dto.MarkOutputDto;
import pl.schoolmanagementsystem.mark.model.Mark;
import pl.schoolmanagementsystem.mark.repository.MarkRepository;
import pl.schoolmanagementsystem.schoolclass.model.SchoolClass;
import pl.schoolmanagementsystem.schoolsubject.model.SchoolSubject;
import pl.schoolmanagementsystem.schoolsubject.service.SchoolSubjectService;
import pl.schoolmanagementsystem.student.model.Student;
import pl.schoolmanagementsystem.student.service.StudentService;
import pl.schoolmanagementsystem.teacher.model.Teacher;
import pl.schoolmanagementsystem.teacher.service.TeacherService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarkService {

    private final MarkRepository markRepository;

    private final StudentService studentService;

    private final TeacherService teacherService;

    private final SchoolSubjectService schoolSubjectService;

    public MarkOutputDto addMarkForStudent(String teacherEmail, MarkInputDto markInputDto, int studentId) {
        Student student = studentService.findById(studentId);
        SchoolClass studentsClass = student.getSchoolClass();
        Teacher teacher = teacherService.findByEmail(teacherEmail);
        SchoolSubject schoolSubject = schoolSubjectService.findByName(markInputDto.getSubject());
        teacherService.makeSureIfTeacherTeachesThisClass(teacher, schoolSubject, studentsClass);
        Mark mark = markRepository.save(MarkBuilder.build(markInputDto.getMark(), student, schoolSubject));
        return MarkMapper.mapMarkToOutputDto(mark);
    }

    public Map<String, List<Integer>> getGroupedMarksBySubjectForStudent(int studentId) {
        studentService.checkIfStudentExists(studentId);
        return groupStudentsMarks(studentId);
    }

    public List<MarkAvgDto> getAverageMarksForStudent(int studentId) {
        studentService.checkIfStudentExists(studentId);
        return markRepository.findAllAverageMarksForStudentById(studentId);
    }

    private Map<String, List<Integer>> groupStudentsMarks(int studentId) {
        List<MarkDtoWithTwoFields> studentsMarks = markRepository.findAllMarksForStudentById(studentId);
        Map<String, List<MarkDtoWithTwoFields>> groupedMarks = groupMarksBySubject(studentsMarks);
        return MarkMapper.mapListOfMarksToIntegersInMapStructure(groupedMarks);
    }

    private Map<String, List<MarkDtoWithTwoFields>> groupMarksBySubject(List<MarkDtoWithTwoFields> studentsMarks) {
        return studentsMarks.stream()
                .collect(Collectors.groupingBy(MarkDtoWithTwoFields::getSubject));
    }
}
