package pl.schoolmanagementsystem.teacher.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.schoolmanagementsystem.email.service.EmailService;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto;
import pl.schoolmanagementsystem.schoolsubject.service.SchoolSubjectService;
import pl.schoolmanagementsystem.teacher.repository.TeacherRepository;
import pl.schoolmanagementsystem.teacherinclass.repository.TeacherInClassRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TeacherServiceTest implements TeacherSamples {

    private EmailService emailService = mock(EmailService.class);

    private SchoolSubjectService schoolSubjectService = mock(SchoolSubjectService.class);

    private TeacherInClassRepository teacherInClassRepository = mock(TeacherInClassRepository.class);

    private TeacherRepository teacherRepository = mock(TeacherRepository.class);

    private PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    private TeacherService teacherService = new TeacherService(
            emailService, schoolSubjectService, teacherInClassRepository, teacherRepository, passwordEncoder);

    @Test
    void should_return_taught_classes_by_teacher_when_teacher_exists() {
        when(teacherRepository.existsById(anyInt())).thenReturn(true);
        when(teacherRepository.findTaughtClassesByTeacher(anyInt())).thenReturn(listOfTaughtClassesByTeacher());

        List<SubjectAndClassOutputDto> actualTaughtClassesByTeacher = teacherService.getTaughtClassesByTeacher(anyInt());

        assertThat(actualTaughtClassesByTeacher).containsExactlyInAnyOrderElementsOf(listOfTaughtClassesByTeacher());
    }

}