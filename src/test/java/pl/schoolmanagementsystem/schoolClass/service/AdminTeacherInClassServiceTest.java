package pl.schoolmanagementsystem.schoolClass.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.exception.ClassAlreadyHasTeacherException;
import pl.schoolmanagementsystem.common.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.common.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.common.exception.NoSuchTeacherException;
import pl.schoolmanagementsystem.common.exception.TeacherDoesNotTeachClassException;
import pl.schoolmanagementsystem.common.exception.TeacherDoesNotTeachSubjectException;
import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.common.model.SchoolSubject;
import pl.schoolmanagementsystem.common.model.Teacher;
import pl.schoolmanagementsystem.common.model.TeacherInClass;
import pl.schoolmanagementsystem.common.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.common.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.repository.TeacherInClassRepository;
import pl.schoolmanagementsystem.common.repository.TeacherRepository;
import pl.schoolmanagementsystem.schoolClass.dto.AddOrRemoveTeacherInClassDto;
import pl.schoolmanagementsystem.schoolClass.dto.TeacherInClassDto;
import pl.schoolmanagementsystem.schoolClass.utils.TeacherInClassMapper;
import pl.schoolmanagementsystem.schoolClass.utils.TeacherInClassMapperStub;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminTeacherInClassServiceTest implements Samples {

    @Mock
    private TeacherInClassRepository teacherInClassRepository;
    @Mock
    private SchoolClassRepository schoolClassRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Spy
    private TeacherInClassMapper teacherInClassMapper = new TeacherInClassMapperStub();
    @Mock
    private SchoolSubjectRepository schoolSubjectRepository;

    @InjectMocks
    private AdminTeacherInClassService adminTeacherInClassService;

    @Test
    void should_correctly_assign_teacher_to_class_when_teacher_in_class_equivalent_is_just_created() {
        Teacher teacher = createTeacherOfBiology();
        TeacherInClass teacherInClass = new TeacherInClass();
        teacherInClass.setTeacher(teacher);
        AddOrRemoveTeacherInClassDto teacherRequest = new AddOrRemoveTeacherInClassDto(ID_1, SUBJECT_BIOLOGY);
        SchoolClass schoolClass = createSchoolClass();
        SchoolSubject schoolSubject = createSchoolSubject();
        when(teacherRepository.findByIdAndFetchSubjects(anyLong())).thenReturn(Optional.of(teacher));
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.ofNullable(schoolClass));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(schoolSubject));
        when(teacherInClassRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        TeacherInClassDto result = adminTeacherInClassService.assignTeacherToSchoolClass(teacherRequest, CLASS_1A);

        assertAll(
                () -> assertThat(result.getTeacherId()).isSameAs(teacherInClass.getTeacher().getId()),
                () -> assertThat(result.getTaughtClasses().size()).isEqualTo(1)
        );
    }

    @Test
    void should_correctly_assign_teacher_to_class_when_teacher_in_class_equivalent_already_exists() {
        Teacher teacher = createTeacherOfBiology();
        TeacherInClass teacherInClass = new TeacherInClass();
        teacherInClass.setTeacher(teacher);
        AddOrRemoveTeacherInClassDto teacherRequest = new AddOrRemoveTeacherInClassDto(ID_1, SUBJECT_BIOLOGY);
        SchoolClass schoolClass = createSchoolClass();
        SchoolSubject schoolSubject = createSchoolSubject();
        when(teacherRepository.findByIdAndFetchSubjects(anyLong())).thenReturn(Optional.of(teacher));
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.ofNullable(schoolClass));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(schoolSubject));
        when(teacherInClassRepository.findByTeacherIdAndTaughtSubject(any(), any())).thenReturn(Optional.of(teacherInClass));
        when(teacherInClassRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        TeacherInClassDto result = adminTeacherInClassService.assignTeacherToSchoolClass(teacherRequest, CLASS_1A);

        assertAll(
                () -> assertThat(result.getTeacherId()).isSameAs(teacherInClass.getTeacher().getId()),
                () -> assertThat(result.getTaughtClasses().size()).isEqualTo(1)
        );
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
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.of(schoolClass));

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
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.empty());

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
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.of(schoolClass));

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
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.of(schoolClass));

        assertThatThrownBy(() -> adminTeacherInClassService.removeTeacherFromSchoolClass(addOrRemoveTeacherInClassDto, "3B"))
                .isInstanceOf(TeacherDoesNotTeachClassException.class)
                .hasMessage("Teacher with id 1 does not teach biology in 3B");
        assertThat(teacher.getTeacherInClasses()).hasSize(1);
    }

    @Test
    void should_throw_exception_when_trying_to_assign_teacher_to_class_but_such_teacher_doesnt_exist() {
        AddOrRemoveTeacherInClassDto teacherRequest = new AddOrRemoveTeacherInClassDto(ID_1, SUBJECT_BIOLOGY);
        when(teacherRepository.findByIdAndFetchSubjects(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminTeacherInClassService.assignTeacherToSchoolClass(teacherRequest, CLASS_1A))
                .isInstanceOf(NoSuchTeacherException.class)
                .hasMessage("Teacher with such an id does not exist: " + teacherRequest.getTeacherId());
        verify(teacherInClassRepository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_trying_to_assign_teacher_to_class_but_such_school_class_doesnt_exist() {
        AddOrRemoveTeacherInClassDto teacherRequest = new AddOrRemoveTeacherInClassDto(ID_1, SUBJECT_BIOLOGY);
        when(teacherRepository.findByIdAndFetchSubjects(anyLong())).thenReturn(Optional.of(new Teacher()));
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminTeacherInClassService.assignTeacherToSchoolClass(teacherRequest, CLASS_1A))
                .isInstanceOf(NoSuchSchoolClassException.class)
                .hasMessage("Such a school class does not exist: " + CLASS_1A);
        verify(teacherInClassRepository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_trying_to_assign_teacher_to_class_but_given_subject_doesnt_exist() {
        AddOrRemoveTeacherInClassDto teacherRequest = new AddOrRemoveTeacherInClassDto(ID_1, SUBJECT_BIOLOGY);
        when(teacherRepository.findByIdAndFetchSubjects(anyLong())).thenReturn(Optional.of(new Teacher()));
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.of(new SchoolClass()));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminTeacherInClassService.assignTeacherToSchoolClass(teacherRequest, CLASS_1A))
                .isInstanceOf(NoSuchSchoolSubjectException.class)
                .hasMessage("Such a school subject does not exist: " + teacherRequest.getTaughtSubject());
        verify(teacherInClassRepository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_trying_to_assign_teacher_to_school_class_but_teacher_doesnt_teach_given_subject() {
        Teacher teacherWithOutSubjects = createTeacherNoSubjectsTaught();
        AddOrRemoveTeacherInClassDto teacherRequest = new AddOrRemoveTeacherInClassDto(ID_1, SUBJECT_BIOLOGY);
        when(teacherRepository.findByIdAndFetchSubjects(anyLong())).thenReturn(Optional.of(teacherWithOutSubjects));
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.of(new SchoolClass()));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(createSchoolSubject()));

        assertThatThrownBy(() -> adminTeacherInClassService.assignTeacherToSchoolClass(teacherRequest, CLASS_1A))
                .isInstanceOf(TeacherDoesNotTeachSubjectException.class)
                .hasMessage("Adam Nowak does not teach " + teacherRequest.getTaughtSubject());
        verify(teacherInClassRepository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_trying_to_assign_teacher_to_school_class_but_the_class_already_has_teacher_of_subject() {
        Teacher teacherWithOutSubjects = createTeacherOfBiology();
        SchoolClass schoolClassWithTeacher = createSchoolClassWithTeacher();
        AddOrRemoveTeacherInClassDto teacherRequest = new AddOrRemoveTeacherInClassDto(ID_1, SUBJECT_BIOLOGY);
        when(teacherRepository.findByIdAndFetchSubjects(anyLong())).thenReturn(Optional.of(teacherWithOutSubjects));
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.of(schoolClassWithTeacher));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(createSchoolSubject()));

        assertThatThrownBy(() -> adminTeacherInClassService.assignTeacherToSchoolClass(teacherRequest, CLASS_1A))
                .isInstanceOf(ClassAlreadyHasTeacherException.class)
                .hasMessage("Alicja Kowalczyk already teaches biology in 1A");
        verify(teacherInClassRepository, never()).save(any());
    }

}
