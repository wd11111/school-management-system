package pl.schoolmanagementsystem.admin.teacher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.admin.mailSender.MailSenderService;
import pl.schoolmanagementsystem.admin.teacher.mapper.TeacherCreator;
import pl.schoolmanagementsystem.common.email.exception.EmailAlreadyInUseException;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.common.schoolSubject.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.common.teacher.TeacherRepository;
import pl.schoolmanagementsystem.common.teacher.dto.TeacherInputDto;
import pl.schoolmanagementsystem.common.teacher.exception.NoSuchTeacherException;
import pl.schoolmanagementsystem.common.teacher.exception.TeacherAlreadyTeachesSubjectException;
import pl.schoolmanagementsystem.common.user.AppUserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminTeacherService {

    private final TeacherRepository teacherRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    private final AppUserRepository userRepository;

    private final MailSenderService mailSenderService;

    private final TeacherCreator teacherCreator;

    @Transactional
    public Teacher createTeacher(TeacherInputDto teacherInputDto) {
        checkIfEmailIsAvailable(teacherInputDto.getEmail());
        Set<SchoolSubject> taughtSubjects = teacherInputDto.getTaughtSubjects().stream()
                .map(subject -> schoolSubjectRepository.findById(subject)
                        .orElseThrow(() -> new NoSuchSchoolSubjectException(subject)))
                .collect(Collectors.toSet());
        Teacher teacher = teacherRepository.save(teacherCreator.createTeacher(teacherInputDto, taughtSubjects));
       // mailSenderService.sendEmail(teacherInputDto.getEmail(), teacher.getAppUser().getToken());
        return teacher;
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Transactional
    public void deleteTeacher(int teacherId) {
        if (!teacherRepository.existsById(teacherId)) {
            throw new NoSuchTeacherException(teacherId);
        }
        teacherRepository.deleteById(teacherId);
    }

    public List<SubjectAndClassDto> getTaughtClassesByTeacher(int id) {
        if (!teacherRepository.existsById(id)) {
            throw new NoSuchTeacherException(id);
        }
        String teacherEmail = teacherRepository.findEmailById(id);
        return teacherRepository.findTaughtClassesByTeacher(teacherEmail);
    }

    @Transactional
    public Teacher addSubjectToTeacher(int teacherId, SchoolSubjectDto schoolSubjectDto) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new NoSuchTeacherException(teacherId));
        SchoolSubject schoolSubject = schoolSubjectRepository.findByNameIgnoreCase(schoolSubjectDto.getSubjectName())
                .orElseThrow(() -> new NoSuchSchoolSubjectException(schoolSubjectDto.getSubjectName()));
        checkIfTeacherDoesntAlreadyTeachSubject(teacher, schoolSubject);
        teacher.addSubject(schoolSubject);
        return teacher;
    }

    private void checkIfEmailIsAvailable(String email) {
        if (userRepository.existsById(email)) {
            throw new EmailAlreadyInUseException(email);
        }
    }

    private void checkIfTeacherDoesntAlreadyTeachSubject(Teacher teacher, SchoolSubject schoolSubject) {
        if (doesTeacherAlreadyTeachSubject(teacher, schoolSubject)) {
            throw new TeacherAlreadyTeachesSubjectException(teacher, schoolSubject);
        }
    }

    private boolean doesTeacherAlreadyTeachSubject(Teacher teacher, SchoolSubject schoolSubject) {
        return teacher.getTaughtSubjects().contains(schoolSubject);
    }
}