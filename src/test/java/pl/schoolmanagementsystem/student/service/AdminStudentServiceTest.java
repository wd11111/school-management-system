package pl.schoolmanagementsystem.student.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.email.service.EmailService;
import pl.schoolmanagementsystem.common.exception.EmailAlreadyInUseException;
import pl.schoolmanagementsystem.common.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.common.exception.NoSuchStudentException;
import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.common.repository.AppUserRepository;
import pl.schoolmanagementsystem.common.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.common.repository.StudentRepository;
import pl.schoolmanagementsystem.common.role.RoleAdder;
import pl.schoolmanagementsystem.student.dto.CreateStudentDto;
import pl.schoolmanagementsystem.student.dto.StudentWithClassDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminStudentServiceTest implements Samples {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SchoolClassRepository schoolClassRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private RoleAdder roleAdder;

    @InjectMocks
    private AdminStudentService adminStudentService;

    @Test
    void should_create_student() {
        CreateStudentDto createStudentDto = CreateStudentDto.builder().schoolClass(CLASS_1A).build();
        SchoolClass schoolClass = createSchoolClass();
        when(userRepository.existsById(any())).thenReturn(false);
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.of(schoolClass));
        doNothing().when(roleAdder).addRoles(any());
        when(studentRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        StudentWithClassDto result = adminStudentService.createStudent(createStudentDto);

        assertThat(result.getSchoolClass()).isEqualTo(createStudentDto.getSchoolClass());
        verify(emailService, times(1)).sendEmail(any(), any());
    }

    @Test
    void should_throw_exception_when_email_is_not_available() {
        CreateStudentDto createStudentDto = CreateStudentDto.builder().email(SURNAME).build();
        when(userRepository.existsById(any())).thenReturn(true);

        assertThatThrownBy(() -> adminStudentService.createStudent(createStudentDto))
                .isInstanceOf(EmailAlreadyInUseException.class)
                .hasMessage("Email already in use: " + SURNAME);
        verify(emailService, never()).sendEmail(any(), any());
        verify(studentRepository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_school_class_not_found() {
        CreateStudentDto createStudentDto = CreateStudentDto.builder().email(SURNAME).schoolClass(CLASS_3B).build();
        when(userRepository.existsById(any())).thenReturn(false);
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminStudentService.createStudent(createStudentDto))
                .isInstanceOf(NoSuchSchoolClassException.class)
                .hasMessage("Such a school class does not exist: " + CLASS_3B);
        verify(emailService, never()).sendEmail(any(), any());
        verify(studentRepository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_deleting_not_existing_student() {
        when(studentRepository.existsById(anyLong())).thenReturn(false);

        assertThatThrownBy(() -> adminStudentService.deleteStudent(ID_2))
                .isInstanceOf(NoSuchStudentException.class)
                .hasMessage("Student with such an id does not exist: " + ID_2);
        verify(studentRepository, never()).deleteById(any());
    }
}