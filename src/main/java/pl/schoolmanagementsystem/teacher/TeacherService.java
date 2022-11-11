package pl.schoolmanagementsystem.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.email.EmailService;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubject;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubjectService;
import pl.schoolmanagementsystem.schoolsubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherInputDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherOutputDto;
import pl.schoolmanagementsystem.teacher.exception.NoSuchTeacherEmailException;
import pl.schoolmanagementsystem.teacher.exception.NoSuchTeacherException;
import pl.schoolmanagementsystem.teacher.exception.TeacherAlreadyTeachesSubjectException;
import pl.schoolmanagementsystem.teacher.exception.TeacherDoesNotTeachSubjectException;
import pl.schoolmanagementsystem.teacher.utils.TeacherMapper;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final EmailService emailService;

    private final SchoolSubjectService schoolSubjectService;


    private final TeacherRepository teacherRepository;

    private final TeacherMapper teacherMapper;

    public List<SubjectAndClassOutputDto> getTaughtClassesByTeacher(int teacherId) {
        checkIfTeacherExists(teacherId);
        return teacherRepository.findTaughtClassesByTeacher(teacherId);
    }

    public TeacherOutputDto createTeacher(TeacherInputDto teacherInputDto) {
        emailService.checkIfEmailIsAvailable(teacherInputDto.getEmail());
        Teacher mappedTeacher = teacherMapper.mapInputDtoToTeacher(teacherInputDto);
        Teacher teacher = teacherRepository.save(mappedTeacher);
        return teacherMapper.mapTeacherToOutputDto(teacher);
    }

    public List<TeacherOutputDto> getAllTeachersInSchool() {
        return teacherRepository.findAll()
                .stream()
                .map(teacherMapper::mapTeacherToOutputDto)
                .sorted(Comparator.comparing(TeacherOutputDto::getSurname))
                .collect(Collectors.toList());
    }

    @Transactional
    public TeacherOutputDto addTaughtSubjectToTeacher(int teacherId, SchoolSubjectDto schoolSubjectDto) {
        Teacher teacher = findById(teacherId);
        SchoolSubject schoolSubject = schoolSubjectService.findByName(schoolSubjectDto.getSubject());
        checkIfTeacherAlreadyTeachesThisSubject(teacher, schoolSubject);
        teacher.getTaughtSubjects().add(schoolSubject);
        return teacherMapper.mapTeacherToOutputDto(teacher);
    }

    @Transactional
    public void deleteTeacher(int teacherId) {
        checkIfTeacherExists(teacherId);
        teacherRepository.deleteById(teacherId);
    }

    public Teacher findById(int id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new NoSuchTeacherException(id));
    }

    public Teacher findByEmail(String email) {
        return teacherRepository.findByEmail_Email(email)
                .orElseThrow(() -> new NoSuchTeacherEmailException(email));
    }

    public void makeSureIfTeacherTeachesThisSubject(Teacher teacher, SchoolSubject schoolSubject) {
        if (!doesTeacherAlreadyTeachThisSubject(teacher, schoolSubject)) {
            throw new TeacherDoesNotTeachSubjectException(teacher, schoolSubject);
        }
    }

    public int getIdFromPrincipals(Principal principal) {
        return findByEmail(principal.getName()).getId();
    }

    private void checkIfTeacherAlreadyTeachesThisSubject(Teacher teacher, SchoolSubject schoolSubject) {
        if (doesTeacherAlreadyTeachThisSubject(teacher, schoolSubject)) {
            throw new TeacherAlreadyTeachesSubjectException(teacher, schoolSubject);
        }
    }

    private boolean doesTeacherAlreadyTeachThisSubject(Teacher teacher, SchoolSubject schoolSubject) {
        return teacher.getTaughtSubjects().contains(schoolSubject);
    }

    private void checkIfTeacherExists(int teacherId) {
        boolean doesTeacherExist = teacherRepository.existsById(teacherId);
        if (!doesTeacherExist) {
            throw new NoSuchTeacherException(teacherId);
        }
    }
}
