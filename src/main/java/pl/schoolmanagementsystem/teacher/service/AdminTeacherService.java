package pl.schoolmanagementsystem.teacher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.common.schoolSubject.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.common.teacher.TeacherRepository;
import pl.schoolmanagementsystem.common.teacher.exception.NoSuchTeacherException;
import pl.schoolmanagementsystem.common.teacher.exception.TeacherAlreadyTeachesSubjectException;
import pl.schoolmanagementsystem.common.user.AppUserRepository;
import pl.schoolmanagementsystem.common.user.exception.EmailAlreadyInUseException;
import pl.schoolmanagementsystem.email.service.EmailSender;
import pl.schoolmanagementsystem.teacher.dto.CreateTeacherDto;
import pl.schoolmanagementsystem.teacher.utils.TeacherCreator;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminTeacherService {

    private final TeacherRepository teacherRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    private final AppUserRepository userRepository;

    private final EmailSender emailSender;

    private final TeacherCreator teacherCreator;

    @Transactional
    public Teacher createTeacher(CreateTeacherDto createTeacherDto) {
        validateEmailIsAvailable(createTeacherDto.getEmail());

        Set<SchoolSubject> taughtSubjects = createTeacherDto.getTaughtSubjects().stream()
                .map(subject -> schoolSubjectRepository.findByNameIgnoreCase(subject)
                        .orElseThrow(() -> new NoSuchSchoolSubjectException(subject)))
                .collect(Collectors.toSet());

        Teacher teacher = teacherCreator.createTeacher(createTeacherDto, taughtSubjects);
        teacherRepository.save(teacher);
        emailSender.sendEmail(createTeacherDto.getEmail(), teacher.getAppUser().getToken());
        return teacher;
    }

    public Page<Teacher> getAllTeachers(Pageable pageable) {
        return teacherRepository.findAll(pageable);
    }

    @Transactional
    public void deleteTeacher(long teacherId) {
        validateTeacherExists(teacherId);
        teacherRepository.deleteById(teacherId);
    }

    public Page<SubjectAndClassDto> getTaughtClassesByTeacher(long teacherId, Pageable pageable) {
        validateTeacherExists(teacherId);
        String teacherEmail = teacherRepository.findEmailById(teacherId);
        return teacherRepository.findTaughtClassesByTeacher(teacherEmail, pageable);
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