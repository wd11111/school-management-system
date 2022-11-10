package pl.schoolmanagementsystem.teacher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.email.model.Email;
import pl.schoolmanagementsystem.exception.*;
import pl.schoolmanagementsystem.mapper.TeacherMapper;
import pl.schoolmanagementsystem.mark.repository.MarkRepository;
import pl.schoolmanagementsystem.mark.model.Mark;
import pl.schoolmanagementsystem.schoolsubject.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.schoolsubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.email.service.EmailService;
import pl.schoolmanagementsystem.student.service.StudentService;
import pl.schoolmanagementsystem.teacher.repository.TeacherRepository;
import pl.schoolmanagementsystem.teacher.dto.TeacherInputDto;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherOutputDto;
import pl.schoolmanagementsystem.schoolclass.model.SchoolClass;
import pl.schoolmanagementsystem.schoolsubject.model.SchoolSubject;
import pl.schoolmanagementsystem.student.model.Student;
import pl.schoolmanagementsystem.teacher.model.Teacher;
import pl.schoolmanagementsystem.teacherinclass.repository.TeacherInClassRepository;
import pl.schoolmanagementsystem.teacherinclass.model.TeacherInClass;
import pl.schoolmanagementsystem.teacherinclass.service.TeacherInClassService;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherInClassService teacherInClassService;

    private final StudentService studentService;

    private final EmailService emailService;

    private final MarkRepository markRepository;

    private final TeacherInClassRepository teacherInClassRepository;

    private final TeacherRepository teacherRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    private final PasswordEncoder passwordEncoder;

    public List<SubjectAndClassOutputDto> getTaughtClassesByTeacher(int teacherId) {
        checkIfTeacherExists(teacherId);
        return teacherInClassRepository.findTaughtClassesByTeacher(teacherId);
    }

    public List<SubjectAndClassOutputDto> getTaughtClassesForTeacher(String teacherEmail) {
        Teacher teacher = findByEmail(teacherEmail);
        return teacherInClassRepository.findTaughtClassesByTeacher(teacher.getId());
    }

    public TeacherOutputDto createTeacher(TeacherInputDto teacherInputDto) {
        checkIfEmailIsAvailable(teacherInputDto);
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
        SchoolSubject schoolSubject = findSubject(schoolSubjectDto.getSubject());
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
        TeacherInClass teacherInClass = getTeacherInClassIfTheTeacherAlreadyHasEquivalent(teacher, schoolSubject, schoolClass);
        if (!doesTeacherTeachThisClass(teacherInClass, schoolClass)) {
            throw new TeacherDoesNotTeachClassException(schoolSubject, schoolClass);
        }
    }

    public Teacher findById(int id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new NoSuchTeacherException(id));
    }

    public void makeSureIfTeacherTeachesThisSubject(Teacher teacher, SchoolSubject schoolSubject) {
        boolean doesTeacherTeachTheSubject = teacher.getTaughtSubjects().contains(schoolSubject);
        if (!doesTeacherTeachTheSubject) {
            throw new TeacherDoesNotTeachSubjectException(teacher, schoolSubject);
        }
    }

    public Teacher findByEmail(String email) {
        return teacherRepository.findByEmail_Email(email).get();
    }

    private void checkIfEmailIsAvailable(TeacherInputDto teacherInputDto) {
        emailService.checkIfEmailIsAvailable(teacherInputDto.getEmail());
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
        if (!doesTeacherExist(teacherId)) {
            throw new NoSuchTeacherException(teacherId);
        }
    }

    private boolean doesTeacherExist(int teacherId) {
        return teacherRepository.existsById(teacherId);
    }

    private Teacher buildTeacher(TeacherInputDto teacherInputDto) {
        return Teacher.builder()
                .name(teacherInputDto.getName())
                .surname(teacherInputDto.getSurname())
                .email(new Email(teacherInputDto.getEmail()))
                .isAdmin(teacherInputDto.isAdmin())
                .password(passwordEncoder.encode(teacherInputDto.getPassword()))
                .taughtSubjects(mapStringsToSetOfSubjects(teacherInputDto.getTaughtSubjects()))
                .teacherInClasses(new HashSet<>())
                .build();
    }

    private SchoolSubject findSubject(String subjectName) {
        return schoolSubjectRepository.findBySubjectName(subjectName)
                .orElseThrow(() -> new NoSuchSchoolSubjectException(subjectName));
    }

    private Student findStudent(int id) {
        return studentService.findById(id);
    }

    private boolean doesTeacherTeachThisClass(TeacherInClass teacherInClass, SchoolClass schoolClass) {
        return teacherInClass.getTaughtClasses().contains(schoolClass);
    }

    private TeacherInClass getTeacherInClassIfTheTeacherAlreadyHasEquivalent(Teacher teacher,
                                                                             SchoolSubject schoolSubject,
                                                                             SchoolClass schoolClass) {
        return teacherInClassService.getTeacherInClassIfTheTeacherAlreadyHasEquivalent(teacher, schoolSubject)
                .orElseThrow(() -> new TeacherDoesNotTeachClassException(schoolSubject, schoolClass));
    }

    private Set<SchoolSubject> mapStringsToSetOfSubjects(Set<String> subjects) {
        return subjects.stream()
                .map(subject -> schoolSubjectRepository.findBySubjectName(subject)
                        .orElseThrow(() -> new NoSuchSchoolSubjectException(subject)))
                .collect(Collectors.toSet());
    }
}
