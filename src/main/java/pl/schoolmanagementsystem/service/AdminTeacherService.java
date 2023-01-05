package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.mapper.TeacherCreator;
import pl.schoolmanagementsystem.model.SchoolSubject;
import pl.schoolmanagementsystem.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.model.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.model.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.model.Teacher;
import pl.schoolmanagementsystem.repository.TeacherRepository;
import pl.schoolmanagementsystem.model.dto.TeacherRequestDto;
import pl.schoolmanagementsystem.exception.NoSuchTeacherException;
import pl.schoolmanagementsystem.exception.TeacherAlreadyTeachesSubjectException;
import pl.schoolmanagementsystem.repository.AppUserRepository;
import pl.schoolmanagementsystem.exception.EmailAlreadyInUseException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminTeacherService {

    private final TeacherRepository teacherRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    private final AppUserRepository userRepository;

    private final EmailService emailService;

    private final TeacherCreator teacherCreator;

    @Transactional
    public Teacher createTeacher(TeacherRequestDto teacherRequestDto) {
        validateEmailIsAvailable(teacherRequestDto.getEmail());
        Set<SchoolSubject> taughtSubjects = teacherRequestDto.getTaughtSubjects().stream()
                .map(subject -> schoolSubjectRepository.findByNameIgnoreCase(subject)
                        .orElseThrow(() -> new NoSuchSchoolSubjectException(subject)))
                .collect(Collectors.toSet());
        Teacher teacher = teacherCreator.createTeacher(teacherRequestDto, taughtSubjects);
        teacherRepository.save(teacher);
        emailService.sendEmail(teacherRequestDto.getEmail(), teacher.getAppUser().getToken());
        return teacher;
    }

    public Page<Teacher> getAllTeachers(Pageable pageable) {
        return teacherRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
    }

    @Transactional
    public void deleteTeacher(long teacherId) {
        validateTeacherExists(teacherId);
        teacherRepository.deleteById(teacherId);
    }

    public Page<SubjectAndClassDto> getTaughtClassesByTeacher(long teacherId, Pageable pageable) {
       validateTeacherExists(teacherId);
        String teacherEmail = teacherRepository.findEmailById(teacherId);
        return teacherRepository.findTaughtClassesByTeacher(teacherEmail, PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize()));// todo a to nie jest masło maślane? nie wystarczyłoby przekazanie tego pageable zamiast tworzyć PageRequest?
    }

    @Transactional
    public Teacher addSubjectToTeacher(long teacherId, SchoolSubjectDto schoolSubjectDto) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new NoSuchTeacherException(teacherId));
        SchoolSubject schoolSubject = schoolSubjectRepository.findByNameIgnoreCase(schoolSubjectDto.getSubjectName())
                .orElseThrow(() -> new NoSuchSchoolSubjectException(schoolSubjectDto.getSubjectName()));
        validateTeacherDoesntAlreadyTeachSubject(teacher, schoolSubject);
        teacher.addSubject(schoolSubject);
        return teacher;
    }

    private void validateTeacherExists(long teacherId) {
        if (!teacherRepository.existsById(teacherId)) {
            throw new NoSuchTeacherException(teacherId);
        }
    }

    private void validateEmailIsAvailable(String email) {
        if (userRepository.existsById(email)) {
            throw new EmailAlreadyInUseException(email);
        }
    }

    private void validateTeacherDoesntAlreadyTeachSubject(Teacher teacher, SchoolSubject schoolSubject) {
        if (doesTeacherAlreadyTeachSubject(teacher, schoolSubject)) {
            throw new TeacherAlreadyTeachesSubjectException(teacher, schoolSubject);
        }
    }

    private boolean doesTeacherAlreadyTeachSubject(Teacher teacher, SchoolSubject schoolSubject) {
        return teacher.getTaughtSubjects().contains(schoolSubject);
    }
}