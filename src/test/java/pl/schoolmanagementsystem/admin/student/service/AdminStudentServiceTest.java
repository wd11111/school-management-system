package pl.schoolmanagementsystem.admin.student.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.service.EmailService;
import pl.schoolmanagementsystem.mapper.StudentCreator;
import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.model.Student;
import pl.schoolmanagementsystem.repository.StudentRepository;
import pl.schoolmanagementsystem.model.dto.StudentRequestDto;
import pl.schoolmanagementsystem.exception.NoSuchStudentException;
import pl.schoolmanagementsystem.repository.AppUserRepository;
import pl.schoolmanagementsystem.exception.EmailAlreadyInUseException;
import pl.schoolmanagementsystem.service.AdminStudentService;

import java.util.Optional;

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
    private StudentCreator studentCreator;

    @InjectMocks
    private AdminStudentService adminStudentService;

    @Test
    void should_create_student() {
        StudentRequestDto studentRequestDto = StudentRequestDto.builder().schoolClassName(CLASS_1A).build();
        SchoolClass schoolClass = createSchoolClass();
        Student student = createStudent2();
        when(userRepository.existsById(any())).thenReturn(false);
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.of(schoolClass));
        when(studentCreator.createStudent(any(), any())).thenReturn(student);
        when(studentRepository.save(any())).thenReturn(student);

        Student result = adminStudentService.createStudent(studentRequestDto);

        assertThat(result).isSameAs(student);
        verify(emailService, times(1)).sendEmail(any(), any());
    }

    @Test
    void should_throw_exception_when_email_is_not_available() {
        StudentRequestDto studentRequestDto = StudentRequestDto.builder().email(SURNAME).build();
        when(userRepository.existsById(any())).thenReturn(true);

        assertThatThrownBy(() -> adminStudentService.createStudent(studentRequestDto))
                .isInstanceOf(EmailAlreadyInUseException.class)
                .hasMessage("Email already in use: " + SURNAME);
        verify(emailService, never()).sendEmail(any(), any());
        verify(studentRepository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_school_class_not_found() {
        StudentRequestDto studentRequestDto = StudentRequestDto.builder().email(SURNAME).schoolClassName(CLASS_3B).build();
        when(userRepository.existsById(any())).thenReturn(false);
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminStudentService.createStudent(studentRequestDto))
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