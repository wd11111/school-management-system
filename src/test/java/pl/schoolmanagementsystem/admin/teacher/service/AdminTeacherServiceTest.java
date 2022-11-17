package pl.schoolmanagementsystem.admin.teacher.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.admin.mailSender.MailSenderService;
import pl.schoolmanagementsystem.admin.teacher.mapper.TeacherCreator;
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
import pl.schoolmanagementsystem.common.user.exception.EmailAlreadyInUseException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminTeacherServiceTest implements Samples {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private SchoolSubjectRepository schoolSubjectRepository;

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private MailSenderService mailSenderService;

    @Mock
    private TeacherCreator teacherCreator;

    @InjectMocks
    private AdminTeacherService adminTeacherService;

    @Test
    void should_create_teacher() {
        TeacherInputDto teacherInputDto = new TeacherInputDto();
        teacherInputDto.setTaughtSubjects(new HashSet<>(Set.of(SUBJECT_BIOLOGY)));
        Teacher teacher = createTeacherNoSubjectsTaught();
        SchoolSubject schoolSubject = createSchoolSubject();
        when(userRepository.existsById(any())).thenReturn(false);
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(schoolSubject));
        when(teacherCreator.createTeacher(any(), any())).thenReturn(teacher);
        when(teacherRepository.save(any())).thenReturn(teacher);

        Teacher result = adminTeacherService.createTeacher(teacherInputDto);

        assertThat(result).isSameAs(teacher);
        verify(mailSenderService, times(1)).sendEmail(any(), any());
    }

    @Test
    void should_throw_exception_when_email_is_not_available() {
        TeacherInputDto teacherInputDto = new TeacherInputDto();
        teacherInputDto.setEmail(NAME);
        when(userRepository.existsById(any())).thenReturn(true);

        assertThatThrownBy(() -> adminTeacherService.createTeacher(teacherInputDto))
                .isInstanceOf(EmailAlreadyInUseException.class)
                .hasMessage("Email already in use: " + NAME);
        verify(teacherRepository, never()).save(any());
        verify(mailSenderService, never()).sendEmail(any(), any());
    }

    @Test
    void should_throw_exception_when_subject_is_not_found() {
        TeacherInputDto teacherInputDto = new TeacherInputDto();
        teacherInputDto.setTaughtSubjects(new HashSet<>(Set.of(SUBJECT_BIOLOGY)));
        when(userRepository.existsById(any())).thenReturn(false);
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminTeacherService.createTeacher(teacherInputDto))
                .isInstanceOf(NoSuchSchoolSubjectException.class)
                .hasMessage("Such a school subject does not exist: Biology");
        verify(teacherRepository, never()).save(any());
        verify(mailSenderService, never()).sendEmail(any(), any());
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
        List<SubjectAndClassDto> listOfTaughtClasses = listOfTaughtClasses();
        when(teacherRepository.existsById(any())).thenReturn(true);
        when(teacherRepository.findEmailById(anyInt())).thenReturn(NAME3);
        when(teacherRepository.findTaughtClassesByTeacher(anyString())).thenReturn(listOfTaughtClasses);

        List<SubjectAndClassDto> result = adminTeacherService.getTaughtClassesByTeacher(ID_1);

        assertThat(result).isSameAs(listOfTaughtClasses);
    }

    @Test
    void should_throw_exception_when_trying_to_get_taught_classes() {
        when(teacherRepository.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> adminTeacherService.getTaughtClassesByTeacher(ID_1))
                .isInstanceOf(NoSuchTeacherException.class)
                .hasMessage("Teacher with such an id does not exist: " + ID_1);
    }

    @Test
    void should_add_subject_to_teacher() {
        Teacher teacher = createTeacherNoSubjectsTaught();
        SchoolSubject schoolSubject = createSchoolSubject();
        SchoolSubjectDto schoolSubjectDto = new SchoolSubjectDto();
        schoolSubjectDto.setSubjectName(SUBJECT_BIOLOGY);
        when(teacherRepository.findById(anyInt())).thenReturn(Optional.of(teacher));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(schoolSubject));

        Teacher result = adminTeacherService.addSubjectToTeacher(ID_1, schoolSubjectDto);

        assertThat(teacher.getTaughtSubjects()).hasSize(1);
        assertThat(result).isSameAs(teacher);
    }

    @Test
    void should_throw_exception_when_teacher_not_found_while_adding_subject() {
        SchoolSubjectDto schoolSubjectDto = new SchoolSubjectDto();
        when(teacherRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminTeacherService.addSubjectToTeacher(ID_1, schoolSubjectDto))
                .isInstanceOf(NoSuchTeacherException.class)
                .hasMessage("Teacher with such an id does not exist: " + ID_1);
    }

    @Test
    void should_throw_exception_when_given_subject_not_found() {
        Teacher teacher = createTeacherNoSubjectsTaught();
        SchoolSubjectDto schoolSubjectDto = new SchoolSubjectDto();
        schoolSubjectDto.setSubjectName(SUBJECT_BIOLOGY);
        when(teacherRepository.findById(anyInt())).thenReturn(Optional.of(teacher));
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
        when(teacherRepository.findById(anyInt())).thenReturn(Optional.of(teacher));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(schoolSubject));

        assertThatThrownBy(() -> adminTeacherService.addSubjectToTeacher(ID_1, schoolSubjectDto))
                .isInstanceOf(TeacherAlreadyTeachesSubjectException.class)
                .hasMessage("Alicja Kowalczyk already teaches Biology");
        assertThat(teacher.getTaughtSubjects()).hasSize(1);
    }
}