package pl.schoolmanagementsystem.teacher.service;

import org.junit.jupiter.api.Test;
import pl.schoolmanagementsystem.email.exception.EmailAlreadyInUseException;
import pl.schoolmanagementsystem.email.service.EmailService;
import pl.schoolmanagementsystem.schoolsubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto;
import pl.schoolmanagementsystem.schoolsubject.service.SchoolSubjectService;
import pl.schoolmanagementsystem.teacher.dto.TeacherOutputDto;
import pl.schoolmanagementsystem.teacher.exception.NoSuchTeacherException;
import pl.schoolmanagementsystem.teacher.model.Teacher;
import pl.schoolmanagementsystem.teacher.repository.TeacherRepository;
import pl.schoolmanagementsystem.teacher.utils.TeacherMapper;
import pl.schoolmanagementsystem.teacherinclass.repository.TeacherInClassRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class TeacherServiceTest implements TeacherSamples {

    private EmailService emailService = mock(EmailService.class);

    private SchoolSubjectService schoolSubjectService = mock(SchoolSubjectService.class);

    private TeacherInClassRepository teacherInClassRepository = mock(TeacherInClassRepository.class);

    private TeacherRepository teacherRepository = mock(TeacherRepository.class);

    private TeacherMapper teacherMapper = mock(TeacherMapper.class);

    private TeacherService teacherService = new TeacherService(
            emailService, schoolSubjectService, teacherInClassRepository, teacherRepository, teacherMapper);

    @Test
    void should_return_taught_classes_by_teacher_when_teacher_exists() {
        List<SubjectAndClassOutputDto> expectedList = listOfTaughtClassesByTeacher();
        when(teacherRepository.existsById(anyInt())).thenReturn(true);
        when(teacherRepository.findTaughtClassesByTeacher(anyInt())).thenReturn(expectedList);

        List<SubjectAndClassOutputDto> actualTaughtClassesByTeacher = teacherService.getTaughtClassesByTeacher(anyInt());

        assertThat(actualTaughtClassesByTeacher).containsExactlyInAnyOrderElementsOf(expectedList);
    }

    @Test
    void should_throw_no_such_teacher_when_teacher_does_not_exist() {
        when(teacherRepository.existsById(anyInt())).thenReturn(false);

        assertThatThrownBy(() -> teacherService.getTaughtClassesByTeacher(1))
                .isInstanceOf(NoSuchTeacherException.class)
                .hasMessageContaining(String.format("Teacher with such an id does not exist: %d", 1));
        verify(teacherRepository, never()).findTaughtClassesByTeacher(anyInt());
    }

    @Test
    void should_correctly_create_teacher_when_email_is_available() {
        when(teacherRepository.save(any(Teacher.class))).thenReturn(createdTeacher());
        when(teacherMapper.mapTeacherToOutputDto(any(Teacher.class))).thenReturn(mappedCreatedTeacher());

        TeacherOutputDto result = teacherService.createTeacher(TeacherInputDto());

        assertThat(result).isEqualTo(mappedCreatedTeacher());
        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }

    @Test
    void should_throw_email_already_in_use_then_trying_to_create_teacher() {
        doThrow(new EmailAlreadyInUseException(TeacherInputDto().getEmail())).when(emailService).checkIfEmailIsAvailable(anyString());

        assertThatThrownBy(() -> teacherService.createTeacher(TeacherInputDto()))
                .isInstanceOf(EmailAlreadyInUseException.class)
                .hasMessageContaining(String.format("Email already in use: %s", TeacherInputDto().getEmail()));
        verify(teacherRepository, never()).save(any(Teacher.class));
        verify(teacherMapper, never()).mapTeacherToOutputDto(any(Teacher.class));
    }

    @Test
    void should_correctly_return_list_of_teachers_output_dto() {
        when(teacherRepository.findAll()).thenReturn(listOfTeachers());
        when(teacherMapper.mapTeacherToOutputDto(any(Teacher.class))).thenReturn(teacherOutputDto());

        List<TeacherOutputDto> result = teacherService.getAllTeachersInSchool();

        assertThat(result.size()).isEqualTo(2);
        verify(teacherRepository, times(1)).findAll();
        verify(teacherMapper, times(2)).mapTeacherToOutputDto(any(Teacher.class));
    }

    @Test
    void should_add_taught_subject_to_teacher_when_teacher_doesnt_teach_the_subject_yet() {
        Teacher teacher = createdTeacher();
        when(teacherRepository.findById(anyInt())).thenReturn(Optional.ofNullable(teacher));
        when(schoolSubjectService.findByName(anyString())).thenReturn(schoolSubject());
        then(teacher.getTaughtSubjects().size()).isZero();

        teacherService.addTaughtSubjectToTeacher(1, new SchoolSubjectDto("Biology"));

        assertThat(teacher.getTaughtSubjects().size()).isEqualTo(1);
    }


}