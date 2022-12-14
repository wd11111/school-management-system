package pl.schoolmanagementsystem.admin.teacher.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.email.service.EmailService;
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
import pl.schoolmanagementsystem.teacher.dto.CreateTeacherDto;
import pl.schoolmanagementsystem.teacher.service.AdminTeacherService;
import pl.schoolmanagementsystem.teacher.utils.TeacherCreator;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminTeacherServiceTest implements Samples {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private SchoolSubjectRepository schoolSubjectRepository;

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private TeacherCreator teacherCreator;

    @InjectMocks
    private AdminTeacherService adminTeacherService;

    @Test
    void should_create_teacher() {
        CreateTeacherDto createTeacherDto = new CreateTeacherDto();
        createTeacherDto.setTaughtSubjects(new HashSet<>(Set.of(SUBJECT_BIOLOGY)));
        Teacher teacher = createTeacherNoSubjectsTaught();
        SchoolSubject schoolSubject = createSchoolSubject();
        when(userRepository.existsById(any())).thenReturn(false);
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(schoolSubject));
        when(teacherCreator.createTeacher(any(), any())).thenReturn(teacher);
        when(teacherRepository.save(any())).thenReturn(teacher);

        Teacher result = adminTeacherService.createTeacher(createTeacherDto);

        assertThat(result).isSameAs(teacher);
        verify(emailService, times(1)).sendEmail(any(), any());
    }

    @Test
    void should_throw_exception_when_email_is_not_available() {
        CreateTeacherDto createTeacherDto = new CreateTeacherDto();
        createTeacherDto.setEmail(NAME);
        when(userRepository.existsById(any())).thenReturn(true);

        assertThatThrownBy(() -> adminTeacherService.createTeacher(createTeacherDto))
                .isInstanceOf(EmailAlreadyInUseException.class)
                .hasMessage("Email already in use: " + NAME);
        verify(teacherRepository, never()).save(any());
        verify(emailService, never()).sendEmail(any(), any());
    }

    @Test
    void should_throw_exception_when_subject_is_not_found() {
        CreateTeacherDto createTeacherDto = new CreateTeacherDto();
        createTeacherDto.setTaughtSubjects(new HashSet<>(Set.of(SUBJECT_BIOLOGY)));
        when(userRepository.existsById(any())).thenReturn(false);
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminTeacherService.createTeacher(createTeacherDto))
                .isInstanceOf(NoSuchSchoolSubjectException.class)
                .hasMessage("Such a school subject does not exist: Biology");
        verify(teacherRepository, never()).save(any());
        verify(emailService, never()).sendEmail(any(), any());
    }

    @Test
    void should_delete_teacher() {
        when(teacherRepository.existsById(any())).thenReturn(true);

        adminTeacherService.deleteTeacher(ID_1);

        verify(teacherRepository, times(1)).deleteById(any());
    }

    @Test
    void should_throw_exception_when_trying_to_delete() {
        when(teacherRepository.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> adminTeacherService.deleteTeacher(ID_1))
                .isInstanceOf(NoSuchTeacherException.class)
                .hasMessage("Teacher with such an id does not exist: " + ID_1);
        verify(teacherRepository, never()).deleteById(any());
    }

    @Test
    void should_return_taught_classes_by_teacher() {
        Page<SubjectAndClassDto> listOfTaughtClasses = new PageImpl<>(listOfTaughtClasses());
        Pageable pageable = PageRequest.of(0,10);
        when(teacherRepository.existsById(any())).thenReturn(true);
        when(teacherRepository.findEmailById(anyLong())).thenReturn(NAME3);
        when(teacherRepository.findTaughtClassesByTeacher(anyString(), any())).thenReturn(listOfTaughtClasses);

        Page<SubjectAndClassDto> result = adminTeacherService.getTaughtClassesByTeacher(ID_1, pageable);

        assertThat(result).isSameAs(listOfTaughtClasses);
    }

    @Test
    void should_throw_exception_when_trying_to_get_taught_classes() {
        Pageable pageable = PageRequest.of(0,10);
        when(teacherRepository.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> adminTeacherService.getTaughtClassesByTeacher(ID_1, pageable))
                .isInstanceOf(NoSuchTeacherException.class)
                .hasMessage("Teacher with such an id does not exist: " + ID_1);
    }

    @Test
    void should_add_subject_to_teacher() {
        Teacher teacher = createTeacherNoSubjectsTaught();
        SchoolSubject schoolSubject = createSchoolSubject();
        SchoolSubjectDto schoolSubjectDto = new SchoolSubjectDto();
        schoolSubjectDto.setSubjectName(SUBJECT_BIOLOGY);
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(schoolSubject));

        Teacher result = adminTeacherService.addSubjectToTeacher(ID_1, schoolSubjectDto);

        assertThat(teacher.getTaughtSubjects()).hasSize(1);
        assertThat(result).isSameAs(teacher);
    }

    @Test
    void should_throw_exception_when_teacher_not_found_while_adding_subject() {
        SchoolSubjectDto schoolSubjectDto = new SchoolSubjectDto();
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminTeacherService.addSubjectToTeacher(ID_1, schoolSubjectDto))
                .isInstanceOf(NoSuchTeacherException.class)
                .hasMessage("Teacher with such an id does not exist: " + ID_1);
    }

    @Test
    void should_throw_exception_when_given_subject_not_found() {
        Teacher teacher = createTeacherNoSubjectsTaught();
        SchoolSubjectDto schoolSubjectDto = new SchoolSubjectDto();
        schoolSubjectDto.setSubjectName(SUBJECT_BIOLOGY);
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminTeacherService.addSubjectToTeacher(ID_1, schoolSubjectDto))
                .isInstanceOf(NoSuchSchoolSubjectException.class)
                .hasMessage("Such a school subject does not exist: Biology");
    }

    @Test
    void should_throw_exception_when_teacher_already_teaches_subject() {
        Teacher teacher = createTeacherOfBiology();
        SchoolSubject schoolSubject = createSchoolSubject();
        SchoolSubjectDto schoolSubjectDto = new SchoolSubjectDto();
        schoolSubjectDto.setSubjectName(SUBJECT_BIOLOGY);
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(schoolSubject));

        assertThatThrownBy(() -> adminTeacherService.addSubjectToTeacher(ID_1, schoolSubjectDto))
                .isInstanceOf(TeacherAlreadyTeachesSubjectException.class)
                .hasMessage("Alicja Kowalczyk already teaches Biology");
        assertThat(teacher.getTaughtSubjects()).hasSize(1);
    }
}