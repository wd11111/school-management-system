package pl.schoolmanagementsystem.teacher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.common.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.email.service.EmailSender;
import pl.schoolmanagementsystem.common.exception.EmailAlreadyInUseException;
import pl.schoolmanagementsystem.common.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.common.exception.NoSuchTeacherException;
import pl.schoolmanagementsystem.common.exception.TeacherAlreadyTeachesSubjectException;
import pl.schoolmanagementsystem.common.model.AppUser;
import pl.schoolmanagementsystem.common.model.SchoolSubject;
import pl.schoolmanagementsystem.common.model.Teacher;
import pl.schoolmanagementsystem.common.repository.AppUserRepository;
import pl.schoolmanagementsystem.common.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.repository.TeacherRepository;
import pl.schoolmanagementsystem.common.role.RoleAdder;
import pl.schoolmanagementsystem.teacher.dto.CreateTeacherDto;
import pl.schoolmanagementsystem.teacher.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherDto;
import pl.schoolmanagementsystem.teacher.utils.TeacherMapper;

import java.util.List;
import java.util.Set;

import static pl.schoolmanagementsystem.common.role.AppUserService.createAppUser;

@Service
@RequiredArgsConstructor
public class AdminTeacherService {

    private final TeacherRepository teacherRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    private final AppUserRepository userRepository;

    private final EmailSender emailSender;

    private final RoleAdder roleAdder;

    private final TeacherMapper teacherMapper;

    @Transactional
    public TeacherDto createTeacher(CreateTeacherDto dto) {
        validateEmailIsAvailable(dto.getEmail());

        Set<SchoolSubject> taughtSubjects = schoolSubjectRepository.findAllByNameIn(dto.getTaughtSubjects());
        validateAllSubjectNamesAreCorrect(taughtSubjects, dto.getTaughtSubjects());

        AppUser appUser = createAppUser(dto.getEmail());
        Teacher teacher = teacherMapper.mapCreateDtoToEntity(dto, taughtSubjects, appUser);
        roleAdder.addRoles(teacher, dto.isAdmin());

        teacherRepository.saveAndFlush(teacher);

        emailSender.sendEmail(dto.getEmail(), teacher.getAppUser().getToken());
        return teacherMapper.mapEntityToDto(teacher);
    }

    public Page<TeacherDto> getAllTeachers(Pageable pageable) {
        List<Teacher> teachers = teacherRepository.findAll();
        return new PageImpl<>(teacherMapper.mapEntitiesToDtos(teachers), pageable, teachers.size());
    }

    @Transactional
    public void deleteTeacher(Long teacherId) {
        validateTeacherExists(teacherId);
        teacherRepository.deleteById(teacherId);
    }

    public Page<SubjectAndClassDto> getTaughtClassesByTeacher(Long teacherId, Pageable pageable) {
        List<SubjectAndClassDto> taughtClassesByTeacher = teacherRepository.findByIdAndFetchClasses(teacherId)
                .orElseThrow(() -> new NoSuchTeacherException(teacherId))
                .getTeacherInClasses().stream()
                .flatMap(teacherInClass -> teacherInClass.getTaughtClasses().stream()
                        .map(schoolClass -> new SubjectAndClassDto(teacherInClass.getTaughtSubject(), schoolClass.getName())))
                .toList();

        return new PageImpl<>(taughtClassesByTeacher, pageable, taughtClassesByTeacher.size());
    }

    @Transactional
    public TeacherDto assignSubjectToTeacher(Long teacherId, SchoolSubjectDto subjectDto) {
        Teacher teacher = teacherRepository.findByIdAndFetchSubjects(teacherId).orElseThrow(() -> new NoSuchTeacherException(teacherId));
        SchoolSubject schoolSubject = schoolSubjectRepository.findByNameIgnoreCase(subjectDto.getSubjectName())
                .orElseThrow(() -> new NoSuchSchoolSubjectException(subjectDto.getSubjectName()));

        validateTeacherDoesntAlreadyTeachSubject(teacher, schoolSubject);

        teacher.assignSubject(schoolSubject);
        return teacherMapper.mapEntityToDto(teacher);
    }

    private void validateAllSubjectNamesAreCorrect(Set<SchoolSubject> taughtSubjects, Set<String> givenSubjects) {
        if (taughtSubjects.size() != givenSubjects.size()) {
            throw new NoSuchSchoolSubjectException();
        }
    }

    private void validateTeacherExists(Long teacherId) {
        if (!teacherRepository.existsById(teacherId)) {
            throw new NoSuchTeacherException(teacherId);
        }
    }

    private void validateEmailIsAvailable(String email) {
        if (userRepository.existsByEmail(email)) {
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