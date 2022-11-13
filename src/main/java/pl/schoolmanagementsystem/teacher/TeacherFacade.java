package pl.schoolmanagementsystem.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.email.EmailService;
import pl.schoolmanagementsystem.schoolclass.SchoolClass;
import pl.schoolmanagementsystem.schoolclass.SchoolClassService;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubject;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubjectService;
import pl.schoolmanagementsystem.schoolsubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherInputDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherOutputDto;
import pl.schoolmanagementsystem.teacherinclass.TeacherInClassMapper;
import pl.schoolmanagementsystem.teacherinclass.TeacherInClassService;
import pl.schoolmanagementsystem.teacherinclass.dto.TeacherInClassInputDto;
import pl.schoolmanagementsystem.teacherinclass.dto.TeacherInClassOutputDto;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherFacade {

    private final TeacherService teacherService;

    private final TeacherMapper teacherMapper;

    private final EmailService emailService;

    private final SchoolClassService schoolClassService;

    private final SchoolSubjectService schoolSubjectService;

    private final TeacherInClassService teacherInClassService;

    private final TeacherInClassMapper teacherInClassMapper;

    public TeacherOutputDto createTeacher(TeacherInputDto teacherInputDto) {
        emailService.checkIfEmailIsAvailable(teacherInputDto.getEmail());
        Teacher mappedTeacher = teacherMapper.mapInputDtoToTeacher(teacherInputDto);
        Teacher teacher = teacherService.saveTeacher(mappedTeacher);
        return teacherMapper.mapTeacherToOutputDto(teacher);
    }

    public List<SubjectAndClassOutputDto> getTaughtClassesByTeacher(int teacherId) {
        teacherService.makeSureTeacherExists(teacherId);
        return teacherService.findTaughtClassesByTeacher(teacherId);
    }

    public List<TeacherOutputDto> getAllTeachersInSchool() {
        return teacherService.findAllTeachersInSchool()
                .stream()
                .map(teacherMapper::mapTeacherToOutputDto)
                .collect(Collectors.toList());
    }

    public TeacherInClassOutputDto addTeacherToSchoolClass(TeacherInClassInputDto teacherInClassInputDto, String schoolClassName) {
        Teacher teacher = teacherService.findByIdOrThrow(teacherInClassInputDto.getTeacherId());
        SchoolClass schoolClass = schoolClassService.findByNameOrThrow(schoolClassName);
        SchoolSubject schoolSubject = schoolSubjectService.findByNameOrThrow(teacherInClassInputDto.getTaughtSubject());
        schoolClassService.makeSureThisClassDoesntHaveTeacherForThisSubject(schoolClass, schoolSubject);
        teacherService.makeSureTeacherTeachesThisSubject(teacher, schoolSubject);
        teacherInClassService.addTeacherToClass(teacher, schoolSubject, schoolClass);
        return teacherInClassMapper.mapTeacherInClassInputToOutputDto(teacherInClassInputDto, schoolClassName);
    }

    public TeacherOutputDto addTaughtSubjectToTeacher(int teacherId, SchoolSubjectDto schoolSubjectDto) {
        Teacher teacher = teacherService.findByIdOrThrow(teacherId);
        SchoolSubject schoolSubject = schoolSubjectService.findByNameOrThrow(schoolSubjectDto.getSubjectName());
        teacherService.checkIfTeacherDoesntAlreadyTeachThisSubject(teacher, schoolSubject);
        teacherService.addSubjectToTeacher(teacher, schoolSubject);
        return teacherMapper.mapTeacherToOutputDto(teacher);
    }

    public void deleteTeacher(int teacherId) {
        teacherService.makeSureTeacherExists(teacherId);
        teacherService.deleteById(teacherId);
    }

    public int getTeacherIdFromPrincipals(Principal principal) {
        return teacherService.getIdFromPrincipals(principal);
    }
}
