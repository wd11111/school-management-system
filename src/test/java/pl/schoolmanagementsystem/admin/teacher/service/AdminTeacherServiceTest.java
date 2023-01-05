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
import pl.schoolmanagementsystem.service.EmailService;
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
import pl.schoolmanagementsystem.service.AdminTeacherService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    private EmailService emailService;

    @Mock
    private TeacherCreator teacherCreator;

    @InjectMocks
    private AdminTeacherService adminTeacherService;

    @Test
    void should_create_teacher() {
        TeacherRequestDto teacherRequestDto = new TeacherRequestDto();
        teacherRequestDto.setTaughtSubjects(new HashSet<>(Set.of(SUBJECT_BIOLOGY)));
        Teacher teacher = createTeacherNoSubjectsTaught();
        SchoolSubject schoolSubject = createSchoolSubject();
        when(userRepository.existsById(any())).thenReturn(false);
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(schoolSubject));
        when(teacherCreator.createTeacher(any(), any())).thenReturn(teacher);
        when(teacherRepository.save(any())).thenReturn(teacher);

        Teacher result = adminTeacherService.createTeacher(teacherRequestDto);

        assertThat(result).isSameAs(teacher);
        verify(emailService, times(1)).sendEmail(any(), any());
    }

    @Test
    void should_throw_exception_when_email_is_not_available() {
        TeacherRequestDto teacherRequestDto = new TeacherRequestDto();
        teacherRequestDto.setEmail(NAME);
        when(userRepository.existsById(any())).thenReturn(true);

        assertThatThrownBy(() -> adminTeacherService.createTeacher(teacherRequestDto))
                .isInstanceOf(EmailAlreadyInUseException.class)
                .hasMessage("Email already in use: " + NAME);
        verify(teacherRepository, never()).save(any());
        verify(emailService, never()).sendEmail(any(), any());
    }

    @Test
    void should_throw_exception_when_subject_is_not_found() {
        TeacherRequestDto teacherRequestDto = new TeacherRequestDto();
        teacherRequestDto.setTaughtSubjects(new HashSet<>(Set.of(SUBJECT_BIOLOGY)));
        when(userRepository.existsById(any())).thenReturn(false);
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminTeacherService.createTeacher(teacherRequestDto))
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