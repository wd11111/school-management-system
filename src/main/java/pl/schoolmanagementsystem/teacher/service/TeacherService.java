package pl.schoolmanagementsystem.teacher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.email.service.EmailService;
import pl.schoolmanagementsystem.exception.NoSuchTeacherException;
import pl.schoolmanagementsystem.exception.TeacherAlreadyTeachesSubjectException;
import pl.schoolmanagementsystem.exception.TeacherDoesNotTeachClassException;
import pl.schoolmanagementsystem.exception.TeacherDoesNotTeachSubjectException;
import pl.schoolmanagementsystem.schoolclass.model.SchoolClass;
import pl.schoolmanagementsystem.schoolsubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto;
import pl.schoolmanagementsystem.schoolsubject.model.SchoolSubject;
import pl.schoolmanagementsystem.schoolsubject.service.SchoolSubjectService;
import pl.schoolmanagementsystem.teacher.dto.TeacherInputDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherOutputDto;
import pl.schoolmanagementsystem.teacher.model.Teacher;
import pl.schoolmanagementsystem.teacher.repository.TeacherRepository;
import pl.schoolmanagementsystem.teacher.utils.TeacherBuilder;
import pl.schoolmanagementsystem.teacher.utils.TeacherMapper;
import pl.schoolmanagementsystem.teacherinclass.model.TeacherInClass;
import pl.schoolmanagementsystem.teacherinclass.repository.TeacherInClassRepository;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final EmailService emailService;

    private final SchoolSubjectService schoolSubjectService;

    private final TeacherInClassRepository teacherInClassRepository;

    private final TeacherRepository teacherRepository;

    private final PasswordEncoder passwordEncoder;

    public List<SubjectAndClassOutputDto> getTaughtClassesByTeacher(int teacherId) {
        checkIfTeacherExists(teacherId);
        return teacherInClassRepository.findTaughtClassesByTeacher(teacherId);
    }

    public TeacherOutputDto createTeacher(TeacherInputDto teacherInputDto) {
        emailService.checkIfEmailIsAvailable(teacherInputDto.getEmail());
        Teacher teacher = teacherRepository.save(buildTeacher(teacherInputDto));
        return TeacherMapper.mapTeacherToOutputDto(teacher);
    }

    public List<TeacherOutputDto> getAllTeachersInSchool() {
        return teacherRepository.findAll()
                .stream()
                .map(TeacherMapper::mapTeacherToOutputDto)
                .sorted(Comparator.comparing(TeacherOutputDto::getSurname))
                .collect(Collectors.toList());
    }

    @Transactional
    public TeacherOutputDto addTaughtSubjectToTeacher(int teacherId, SchoolSubjectDto schoolSubjectDto) {
        Teacher teacher = findById(teacherId);
        SchoolSubject schoolSubject = schoolSubjectService.findByName(schoolSubjectDto.getSubject());
        checkIfTeacherAlreadyTeachesThisSubject(teacher, schoolSubject);
        teacher.getTaughtSubjects().add(schoolSubject);
        return TeacherMapper.mapTeacherToOutputDto(teacher);
    }

    @Transactional
    public void deleteTeacher(int teacherId) {
        checkIfTeacherExists(teacherId);
        teacherRepository.deleteById(teacherId);
    }

    public void makeSureIfTeacherTeachesThisClass(Teacher teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        if (teacher.isAdmin()) {
            return;
        }
        TeacherInClass teacherInClass = getTeacherInClassIfTheTeacherAlreadyHasEquivalent(teacher, schoolSubject, schoolClass);
        boolean doesTeacherTeachThisClass = teacherInClass.getTaughtClasses().contains(schoolClass);
        if (!doesTeacherTeachThisClass) {
            throw new TeacherDoesNotTeachClassException(schoolSubject, schoolClass);
        }
    }

    public Teacher findById(int id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new NoSuchTeacherException(id));
    }

    public Teacher findByEmail(String email) {
        return teacherRepository.findByEmail_Email(email).get();
    }

    public void makeSureIfTeacherTeachesThisSubject(Teacher teacher, SchoolSubject schoolSubject) {
        if (!doesTeacherAlreadyTeachesThisSubject(teacher, schoolSubject)) {
            throw new TeacherDoesNotTeachSubjectException(teacher, schoolSubject);
        }
    }

    public int getIdFromPrincipals(Principal principal) {
        return findByEmail(principal.getName()).getId();
    }

    private void checkIfTeacherAlreadyTeachesThisSubject(Teacher teacher, SchoolSubject schoolSubject) {
        if (doesTeacherAlreadyTeachesThisSubject(teacher, schoolSubject)) {
            throw new TeacherAlreadyTeachesSubjectException(teacher, schoolSubject);
        }
    }

    private boolean doesTeacherAlreadyTeachesThisSubject(Teacher teacher, SchoolSubject schoolSubject) {
        return teacher.getTaughtSubjects().contains(schoolSubject);
    }

    private void checkIfTeacherExists(int teacherId) {
        boolean doesTeacherExist = teacherRepository.existsById(teacherId);
        if (!doesTeacherExist) {
            throw new NoSuchTeacherException(teacherId);
        }
    }

    private TeacherInClass getTeacherInClassIfTheTeacherAlreadyHasEquivalent(Teacher teacher,
                                                                             SchoolSubject schoolSubject,
                                                                             SchoolClass schoolClass) {
        return teacherInClassRepository.findByTeacherAndTaughtSubject(teacher, schoolSubject)
                .orElseThrow(() -> new TeacherDoesNotTeachClassException(schoolSubject, schoolClass));
    }

    private Teacher buildTeacher(TeacherInputDto teacherInputDto) {
        return TeacherBuilder.build(teacherInputDto, passwordEncoder, schoolSubjectService);
    }
}
