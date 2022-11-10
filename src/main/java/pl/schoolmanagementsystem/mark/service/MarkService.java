package pl.schoolmanagementsystem.mark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
        Mark mark = markRepository.save(MarkBuilder.buildMark(markInputDto.getMark(), student, schoolSubject));
        return MarkMapper.mapMarkToOutputDto(mark);
    }
}
