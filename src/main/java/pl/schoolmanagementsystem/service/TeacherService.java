package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.exception.*;
import pl.schoolmanagementsystem.mapper.MarkMapper;
import pl.schoolmanagementsystem.mapper.TeacherMapper;
import pl.schoolmanagementsystem.model.*;
import pl.schoolmanagementsystem.model.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.model.dto.input.MarkInputDto;
import pl.schoolmanagementsystem.model.dto.input.TeacherInputDto;
import pl.schoolmanagementsystem.model.dto.output.MarkOutputDto;
import pl.schoolmanagementsystem.model.dto.output.SubjectAndClassOutputDto;
import pl.schoolmanagementsystem.model.dto.output.TeacherOutputDto;
import pl.schoolmanagementsystem.repository.*;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final StudentRepository studentRepository;

    private final EmailRepository emailRepository;

    private final MarkRepository markRepository;

    private final TeacherInClassRepository teacherInClassRepository;

    private final TeacherRepository teacherRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    public MarkOutputDto addMark(MarkInputDto markInputDto, int studentId) {
        Student student = findStudent(studentId);
        SchoolClass studentsClass = student.getSchoolClass();
        Teacher teacher = findTeacher(markInputDto.getTeacherId());
        SchoolSubject schoolSubject = findSubject(markInputDto.getSubject());
        checkIfTeacherTeachesThisClass(teacher, schoolSubject, studentsClass);
        Mark mark = markRepository.save(buildMark(markInputDto.getMark(), student, schoolSubject));
        return MarkMapper.mapMarkToOutputDto(mark);
    }

    public List<SubjectAndClassOutputDto> getTaughtClassesByTeacher(int teacherId) {
        return teacherInClassRepository.findTaughtClassesByTeacher(teacherId);
    }

    public TeacherOutputDto createTeacher(TeacherInputDto teacherInputDto) {
        checkIfEmailIsAvailable(teacherInputDto.getEmail());
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
        Teacher teacher = findTeacher(teacherId);
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

    private Mark buildMark(int mark, Student student, SchoolSubject schoolSubject) {
        return Mark.builder()
                .mark(mark)
                .student(student)
                .subject(schoolSubject)
                .build();
    }

    public boolean isEmailAvailable(String email) {
        return emailRepository.existsById(email);
    }

    public void checkIfEmailIsAvailable(String email) {
        if (isEmailAvailable(email)) {
            throw new EmailAlreadyInUseException(email);
        }
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
        if (doesTeacherExist(teacherId)) {
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
                .teacherInClasses(new HashSet<>())
                .taughtSubjects(mapStringsToSetOfSubjects(teacherInputDto.getTaughtSubjects()))
                .email(new Email(teacherInputDto.getEmail()))
                .build();
    }

    private SchoolSubject findSubject(String subjectName) {
        return schoolSubjectRepository.findBySubjectName(subjectName)
                .orElseThrow(() -> new NoSuchSchoolSubjectException(subjectName));
    }

    private Student findStudent(int id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchStudentException(id));
    }

    private Teacher findTeacher(int id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new NoSuchTeacherException(id));
    }

    private void checkIfTeacherTeachesThisClass(Teacher teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        TeacherInClass teacherInClass = getTeacherInClassIfTheTeacherAlreadyHasEquivalent(teacher, schoolSubject, schoolClass);
        if (doesTeacherTeachThisClass(teacherInClass, schoolClass)) {
            throw new TeacherDoesNotTeachClassException(schoolSubject, schoolClass);
        }
    }

    private boolean doesTeacherTeachThisClass(TeacherInClass teacherInClass, SchoolClass schoolClass) {
        return teacherInClass.getTaughtClasses().contains(schoolClass);
    }

    private TeacherInClass getTeacherInClassIfTheTeacherAlreadyHasEquivalent(Teacher teacher,
                                                                             SchoolSubject schoolSubject,
                                                                             SchoolClass schoolClass) {
        return teacherInClassRepository.findByTeacherAndTaughtSubject(teacher, schoolSubject)
                .orElseThrow(() -> new TeacherDoesNotTeachClassException(schoolSubject, schoolClass));
    }

    private Set<SchoolSubject> mapStringsToSetOfSubjects(Set<String> subjects) {
        return subjects.stream()
                .map(subject -> schoolSubjectRepository.findBySubjectName(subject)
                        .orElseThrow(() -> new NoSuchSchoolSubjectException(subject)))
                .collect(Collectors.toSet());
    }
}
