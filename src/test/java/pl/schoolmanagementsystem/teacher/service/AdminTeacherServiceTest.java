package pl.schoolmanagementsystem.teacher.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.email.service.EmailService;
import pl.schoolmanagementsystem.common.exception.EmailAlreadyInUseException;
import pl.schoolmanagementsystem.common.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.common.exception.NoSuchTeacherException;
import pl.schoolmanagementsystem.common.exception.TeacherAlreadyTeachesSubjectException;
import pl.schoolmanagementsystem.common.model.SchoolSubject;
import pl.schoolmanagementsystem.common.model.Teacher;
import pl.schoolmanagementsystem.common.model.TeacherInClass;
import pl.schoolmanagementsystem.common.repository.AppUserRepository;
import pl.schoolmanagementsystem.common.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.repository.TeacherRepository;
import pl.schoolmanagementsystem.common.role.RoleAdder;
import pl.schoolmanagementsystem.teacher.dto.CreateTeacherDto;
import pl.schoolmanagementsystem.teacher.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherDto;
import pl.schoolmanagementsystem.teacher.utils.TeacherMapper;
import pl.schoolmanagementsystem.teacher.utils.TeacherMapperStub;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
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
    private RoleAdder roleAdder;

    @Spy
    private TeacherMapper teacherMapper = new TeacherMapperStub();

    @InjectMocks
    private AdminTeacherService adminTeacherService;

    @Test
    void should_correctly_create_teacher() {
        CreateTeacherDto createTeacherDto = createCreateTeacherDto();
        createTeacherDto.setTaughtSubjects(new HashSet<>(Set.of(SUBJECT_BIOLOGY)));
        when(userRepository.existsById(any())).thenReturn(false);
        when(schoolSubjectRepository.findAllByNameIn(any())).thenReturn(Set.of(createSchoolSubject()));
        doNothing().when(roleAdder).addRoles(any(), anyBoolean());
        when(teacherRepository.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        TeacherDto result = adminTeacherService.createTeacher(createTeacherDto);

        assertThat(result).usingRecursiveComparison()
                .ignoringFields("id")
                .comparingOnlyFields("name", "surname")
                .isEqualTo(createTeacherDto);
        verify(emailService, times(1)).sendEmail(any(), any());
    }

    @Test
    void should_throw_exception_when_trying_to_create_teacher_but_given_email_is_not_available() {
        CreateTeacherDto createTeacherDto = new CreateTeacherDto();
        createTeacherDto.setEmail(NAME);
        when(userRepository.existsById(any())).thenReturn(true);

        assertThatThrownBy(() -> adminTeacherService.createTeacher(createTeacherDto))
                .isInstanceOf(EmailAlreadyInUseException.class)
                .hasMessage("Email already in use: " + NAME);
        verify(teacherRepository, never()).saveAndFlush(any());
        verify(emailService, never()).sendEmail(any(), any());
    }

    @Test
    void should_throw_exception_when_trying_to_create_teacher_but_given_taguht_subject_is_not_found() {
        CreateTeacherDto createTeacherDto = new CreateTeacherDto();
        createTeacherDto.setTaughtSubjects(new HashSet<>(Set.of(SUBJECT_BIOLOGY)));
        when(userRepository.existsById(any())).thenReturn(false);
        when(schoolSubjectRepository.findAllByNameIn(any())).thenReturn(Collections.emptySet());

        assertThatThrownBy(() -> adminTeacherService.createTeacher(createTeacherDto))
                .isInstanceOf(NoSuchSchoolSubjectException.class)
                .hasMessage("Some of given subjects does not exist");
        verify(teacherRepository, never()).saveAndFlush(any());
        verify(emailService, never()).sendEmail(any(), any());
    }

    @Test
    void should_correctly_delete_teacher() {
        when(teacherRepository.existsById(any())).thenReturn(true);

        adminTeacherService.deleteTeacher(ID_1);

        verify(teacherRepository, times(1)).deleteById(any());
    }

    @Test
    void should_throw_exception_when_trying_to_delete_not_existing_teacher() {
        when(teacherRepository.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> adminTeacherService.deleteTeacher(ID_1))
                .isInstanceOf(NoSuchTeacherException.class)
                .hasMessage("Teacher with such an id does not exist: " + ID_1);
        verify(teacherRepository, never()).deleteById(any());
    }

    @Test
    void should_return_taught_classes_by_teacher() {
        Teacher teacher = createTeacherOfBiology();
        teacher.setTeacherInClasses(Set.of(TeacherInClass.builder()
                .taughtClasses(Set.of(createSchoolClass()))
                .taughtSubject(SUBJECT_BIOLOGY)
                .build()));
        Pageable pageable = PageRequest.of(0, 10);
        when(teacherRepository.findByIdAndFetchClasses(any())).thenReturn(Optional.of(teacher));

        Page<SubjectAndClassDto> result = adminTeacherService.getTaughtClassesByTeacher(ID_1, pageable);

        assertThat(result).extracting("schoolSubject", "schoolClass")
                .containsExactlyElementsOf(List.of(tuple(SUBJECT_BIOLOGY, CLASS_1A)));
    }

    @Test
    void should_throw_exception_when_trying_to_get_taught_classes_by_teacher_but_could_not_find_teacher_with_given_id() {
        Pageable pageable = PageRequest.of(0, 10);
        when(teacherRepository.findByIdAndFetchClasses(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminTeacherService.getTaughtClassesByTeacher(ID_1, pageable))
                .isInstanceOf(NoSuchTeacherException.class)
                .hasMessage("Teacher with such an id does not exist: " + ID_1);
    }

    @Test
    void should_correctly_assign_subject_to_teacher() {
        Teacher teacher = createTeacherNoSubjectsTaught();
        SchoolSubject schoolSubject = createSchoolSubject();
        SchoolSubjectDto schoolSubjectDto = new SchoolSubjectDto();
        schoolSubjectDto.setSubjectName(SUBJECT_BIOLOGY);
        when(teacherRepository.findByIdAndFetchSubjects(anyLong())).thenReturn(Optional.of(teacher));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(schoolSubject));

        TeacherDto result = adminTeacherService.assignSubjectToTeacher(ID_1, schoolSubjectDto);

        assertThat(teacher.getTaughtSubjects()).hasSize(1);
        assertThat(result).usingRecursiveComparison()
                .comparingOnlyFields("id", "name", "surname")
                .isEqualTo(teacher);
    }

    @Test
    void should_throw_exception_when_trying_to_assign_subject_to_not_existing_teacher() {
        SchoolSubjectDto schoolSubjectDto = new SchoolSubjectDto();
        when(teacherRepository.findByIdAndFetchSubjects(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminTeacherService.assignSubjectToTeacher(ID_1, schoolSubjectDto))
                .isInstanceOf(NoSuchTeacherException.class)
                .hasMessage("Teacher with such an id does not exist: " + ID_1);
    }

    @Test
    void should_throw_exception_when_trying_to_assign_subject_to_teacher_but_given_subject_is_not_found() {
        Teacher teacher = createTeacherNoSubjectsTaught();
        SchoolSubjectDto schoolSubjectDto = new SchoolSubjectDto();
        schoolSubjectDto.setSubjectName(SUBJECT_BIOLOGY);
        when(teacherRepository.findByIdAndFetchSubjects(anyLong())).thenReturn(Optional.of(teacher));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminTeacherService.assignSubjectToTeacher(ID_1, schoolSubjectDto))
                .isInstanceOf(NoSuchSchoolSubjectException.class)
                .hasMessage("Such a school subject does not exist: biology");
    }

    @Test
    void should_throw_exception_when_trying_to_assign_subject_to_teacher_but_teacher_already_teaches_one() {
        Teacher teacher = createTeacherOfBiology();
        SchoolSubject schoolSubject = createSchoolSubject();
        SchoolSubjectDto schoolSubjectDto = new SchoolSubjectDto();
        schoolSubjectDto.setSubjectName(SUBJECT_BIOLOGY);
        when(teacherRepository.findByIdAndFetchSubjects(anyLong())).thenReturn(Optional.of(teacher));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(schoolSubject));

        assertThatThrownBy(() -> adminTeacherService.assignSubjectToTeacher(ID_1, schoolSubjectDto))
                .isInstanceOf(TeacherAlreadyTeachesSubjectException.class)
                .hasMessage("Alicja Kowalczyk already teaches biology");
        assertThat(teacher.getTaughtSubjects()).hasSize(1);
    }
}