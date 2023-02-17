package pl.schoolmanagementsystem.schoolClass.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.exception.*;
import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.common.model.Teacher;
import pl.schoolmanagementsystem.common.model.TeacherInClass;
import pl.schoolmanagementsystem.common.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.common.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.repository.TeacherInClassRepository;
import pl.schoolmanagementsystem.common.repository.TeacherRepository;
import pl.schoolmanagementsystem.schoolClass.dto.AddOrRemoveTeacherInClassDto;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminTeacherInClassServiceTest implements Samples {

    @Mock
    private TeacherInClassRepository teacherInClassRepository;
    @Mock
    private SchoolClassRepository classRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private SchoolSubjectRepository schoolSubjectRepository;

    @InjectMocks
    private AdminTeacherInClassService adminTeacherInClassService;

    @Test
    void should_correctly_add_teacher_to_class_when_its_teacher_in_class_already_exists() {
        Teacher teacher = createTeacherNoSubjectsTaught();
        SchoolClass schoolClass = createSchoolClass();
        TeacherInClass teacherInClass = new TeacherInClass();
        teacherInClass.setTaughtClasses(new HashSet<>());
        teacherInClass.setTaughtSubject(SUBJECT_BIOLOGY);
        when(teacherInClassRepository.findByTeacherIdAndTaughtSubject(any(), any())).thenReturn(Optional.of(teacherInClass));

        adminTeacherInClassService.assignTeacherToClass(teacher, SUBJECT_BIOLOGY, schoolClass);

        assertThat(teacherInClass.getTaughtClasses()).hasSize(1);
        verify(teacherInClassRepository, times(1)).save(any());
    }

    @Test
    void should_correctly_remove_teacher_from_school_class() {
        AddOrRemoveTeacherInClassDto addOrRemoveTeacherInClassDto = new AddOrRemoveTeacherInClassDto(1L, "biology");
        TeacherInClass teacherInClass = createTeacherInClass();
        Teacher teacher = teacherInClass.getTeacher();
        teacher.getTeacherInClasses().add(teacherInClass);
        SchoolClass schoolClass = teacherInClass.getTaughtClasses().stream().findFirst().get();
        when(schoolSubjectRepository.existsById(anyString())).thenReturn(true);
        when(teacherRepository.findByIdAndFetchClasses(anyLong())).thenReturn(Optional.of(teacher));
        when(classRepository.findById(anyString())).thenReturn(Optional.of(schoolClass));

        adminTeacherInClassService.removeTeacherFromSchoolClass(addOrRemoveTeacherInClassDto, schoolClass.getName());

        assertThat(teacherInClass.getTaughtClasses()).hasSize(0);
    }

    @Test
    void should_throw_exception_when_trying_to_remove_teacher_from_class_but_given_subject_doesnt_exist() {
        AddOrRemoveTeacherInClassDto addOrRemoveTeacherInClassDto = new AddOrRemoveTeacherInClassDto(1L, "biology");
        when(schoolSubjectRepository.existsById(anyString())).thenReturn(false);

        assertThatThrownBy(() -> adminTeacherInClassService.removeTeacherFromSchoolClass(addOrRemoveTeacherInClassDto, "1A"))
                .isInstanceOf(NoSuchSchoolSubjectException.class)
                .hasMessage("Such a school subject does not exist: " + addOrRemoveTeacherInClassDto.getTaughtSubject());
        verify(teacherRepository, never()).findByIdAndFetchClasses(anyLong());
    }

    @Test
    void should_throw_exception_when_trying_to_remove_teacher_from_class_but_given_teacher_doesnt_exist() {
        AddOrRemoveTeacherInClassDto addOrRemoveTeacherInClassDto = new AddOrRemoveTeacherInClassDto(1L, "biology");
        when(schoolSubjectRepository.existsById(anyString())).thenReturn(true);
        when(teacherRepository.findByIdAndFetchClasses(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminTeacherInClassService.removeTeacherFromSchoolClass(addOrRemoveTeacherInClassDto, "1A"))
                .isInstanceOf(NoSuchTeacherException.class)
                .hasMessage("Teacher with such an id does not exist: " + 1);
    }

    @Test
    void should_throw_exception_when_trying_to_remove_teacher_from_class_but_given_school_class_doesnt_exist() {
        AddOrRemoveTeacherInClassDto addOrRemoveTeacherInClassDto = new AddOrRemoveTeacherInClassDto(1L, "biology");
        TeacherInClass teacherInClass = createTeacherInClass();
        Teacher teacher = teacherInClass.getTeacher();
        teacher.getTeacherInClasses().add(teacherInClass);
        when(schoolSubjectRepository.existsById(anyString())).thenReturn(true);
        when(teacherRepository.findByIdAndFetchClasses(anyLong())).thenReturn(Optional.of(teacher));
        when(classRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminTeacherInClassService.removeTeacherFromSchoolClass(addOrRemoveTeacherInClassDto, "1A"))
                .isInstanceOf(NoSuchSchoolClassException.class)
                .hasMessage("Such a school class does not exist: " + "1A");
        assertThat(teacher.getTeacherInClasses()).hasSize(1);
    }

    @Test
    void should_throw_exception_when_teacher_doesnt_teach_subject_when_trying_to_remove_teacher_from_school_class() {
        AddOrRemoveTeacherInClassDto addOrRemoveTeacherInClassDto = new AddOrRemoveTeacherInClassDto(1L, "biology");
        TeacherInClass teacherInClass = createTeacherInClass2();
        Teacher teacher = teacherInClass.getTeacher();
        teacher.getTeacherInClasses().add(teacherInClass);
        SchoolClass schoolClass = teacherInClass.getTaughtClasses().stream().findFirst().get();
        when(schoolSubjectRepository.existsById(anyString())).thenReturn(true);
        when(teacherRepository.findByIdAndFetchClasses(anyLong())).thenReturn(Optional.of(teacher));
        when(classRepository.findById(anyString())).thenReturn(Optional.of(schoolClass));

        assertThatThrownBy(() -> adminTeacherInClassService.removeTeacherFromSchoolClass(addOrRemoveTeacherInClassDto, "1A"))
                .isInstanceOf(TeacherDoesNotTeachSubjectException.class)
                .hasMessage("Alicja Kowalczyk does not teach biology");
        assertThat(teacher.getTeacherInClasses()).hasSize(1);
    }

    @Test
    void should_throw_exception_when_teacher_doesnt_teach_class_when_trying_to_remove_teacher_from_school_class() {
        AddOrRemoveTeacherInClassDto addOrRemoveTeacherInClassDto = new AddOrRemoveTeacherInClassDto(1L, "biology");
        TeacherInClass teacherInClass = createTeacherInClass();
        Teacher teacher = teacherInClass.getTeacher();
        teacher.getTeacherInClasses().add(teacherInClass);
        SchoolClass schoolClass = createSchoolClass2();
        when(schoolSubjectRepository.existsById(anyString())).thenReturn(true);
        when(teacherRepository.findByIdAndFetchClasses(anyLong())).thenReturn(Optional.of(teacher));
        when(classRepository.findById(anyString())).thenReturn(Optional.of(schoolClass));

        assertThatThrownBy(() -> adminTeacherInClassService.removeTeacherFromSchoolClass(addOrRemoveTeacherInClassDto, "3b"))
                .isInstanceOf(TeacherDoesNotTeachClassException.class)
                .hasMessage("Teacher with id 1 does not teach biology in 3b");
        assertThat(teacher.getTeacherInClasses()).hasSize(1);
    }

}