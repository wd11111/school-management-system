package pl.schoolmanagementsystem.schoolClass.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.exception.*;
import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.common.model.SchoolSubject;
import pl.schoolmanagementsystem.common.model.Teacher;
import pl.schoolmanagementsystem.common.model.TeacherInClass;
import pl.schoolmanagementsystem.common.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.common.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.repository.StudentRepository;
import pl.schoolmanagementsystem.common.repository.TeacherRepository;
import pl.schoolmanagementsystem.schoolClass.dto.AddOrRemoveTeacherInClassDto;
import pl.schoolmanagementsystem.schoolClass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.schoolClass.dto.TeacherInClassDto;
import pl.schoolmanagementsystem.schoolClass.utils.SchoolClassMapper;
import pl.schoolmanagementsystem.schoolClass.utils.SchoolClassMapperStub;
import pl.schoolmanagementsystem.schoolClass.utils.TeacherInClassMapper;
import pl.schoolmanagementsystem.schoolClass.utils.TeacherInClassMapperStub;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminClassServiceTest implements Samples {

    public static final String CLASS_NAME = "1A";
    @Mock
    private SchoolClassRepository schoolClassRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private SchoolSubjectRepository schoolSubjectRepository;

    @Mock
    private AdminTeacherInClassService teacherInClassService;

    @Mock
    private StudentRepository studentRepository;

    @Spy
    private SchoolClassMapper schoolClassMapper = new SchoolClassMapperStub();

    @Spy
    private TeacherInClassMapper teacherInClassMapper = new TeacherInClassMapperStub();

    @InjectMocks
    private AdminClassService adminClassService;

    @Test
    void should_correctly_create_school_class() {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName(CLASS_NAME);
        when(schoolClassRepository.existsById(anyString())).thenReturn(false);
        when(schoolClassRepository.save(any())).thenReturn(schoolClass);
        SchoolClassDto schoolClassDto = new SchoolClassDto(CLASS_NAME);

        SchoolClass result = adminClassService.createSchoolClass(schoolClassDto);

        assertThat(result).isSameAs(schoolClass);
    }

    @Test
    void should_throw_exception_when_creating_school_class_but_name_already_taken() {
        SchoolClassDto schoolClassDto = new SchoolClassDto(CLASS_NAME);
        when(schoolClassRepository.existsById(anyString())).thenReturn(true);

        assertThatThrownBy(() -> adminClassService.createSchoolClass(schoolClassDto))
                .isInstanceOf(ClassAlreadyExistsException.class)
                .hasMessage("Class 1A already exists");
        verify(schoolClassRepository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_trying_to_get_taught_subjects_in_class_but_class_doesnt_exist() {
        when(schoolClassRepository.existsById(anyString())).thenReturn(false);

        assertThatThrownBy(() -> adminClassService.getTaughtSubjectsInClass(CLASS_NAME))
                .isInstanceOf(NoSuchSchoolClassException.class)
                .hasMessage("Such a school class does not exist: 1A");
    }

    @Test
    void should_correctly_assign_teacher_to_school_class() {
        Teacher teacher = createTeacherOfBiology();
        TeacherInClass teacherInClass = new TeacherInClass();
        teacherInClass.setTeacher(teacher);
        AddOrRemoveTeacherInClassDto teacherRequest = new AddOrRemoveTeacherInClassDto(ID_1, SUBJECT_BIOLOGY);
        SchoolClass schoolClass = createSchoolClass();
        SchoolSubject schoolSubject = createSchoolSubject();
        when(teacherRepository.findByIdAndFetchSubjects(anyLong())).thenReturn(Optional.ofNullable(teacher));
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.ofNullable(schoolClass));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.ofNullable(schoolSubject));
        when(teacherInClassService.assignTeacherToClass(any(), any(), any())).thenReturn(teacherInClass);

        TeacherInClassDto result = adminClassService.assignTeacherToSchoolClass(teacherRequest, CLASS_1A);

        assertThat(result.getTeacherId()).isSameAs(teacherInClass.getTeacher().getId());
    }

    @Test
    void should_throw_exception_when_trying_to_assign_teacher_to_class_but_such_teacher_doesnt_exist() {
        AddOrRemoveTeacherInClassDto teacherRequest = new AddOrRemoveTeacherInClassDto(ID_1, SUBJECT_BIOLOGY);
        when(teacherRepository.findByIdAndFetchSubjects(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminClassService.assignTeacherToSchoolClass(teacherRequest, CLASS_1A))
                .isInstanceOf(NoSuchTeacherException.class)
                .hasMessage("Teacher with such an id does not exist: " + teacherRequest.getTeacherId());
        verify(teacherInClassService, never()).assignTeacherToClass(any(), any(), any());
    }

    @Test
    void should_throw_exception_when_trying_to_assign_teacher_to_class_but_such_school_class_doesnt_exist() {
        AddOrRemoveTeacherInClassDto teacherRequest = new AddOrRemoveTeacherInClassDto(ID_1, SUBJECT_BIOLOGY);
        when(teacherRepository.findByIdAndFetchSubjects(anyLong())).thenReturn(Optional.of(new Teacher()));
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminClassService.assignTeacherToSchoolClass(teacherRequest, CLASS_1A))
                .isInstanceOf(NoSuchSchoolClassException.class)
                .hasMessage("Such a school class does not exist: " + CLASS_1A);
        verify(teacherInClassService, never()).assignTeacherToClass(any(), any(), any());
    }

    @Test
    void should_throw_exception_when_trying_to_assign_teacher_to_class_but_given_subject_doesnt_exist() {
        AddOrRemoveTeacherInClassDto teacherRequest = new AddOrRemoveTeacherInClassDto(ID_1, SUBJECT_BIOLOGY);
        when(teacherRepository.findByIdAndFetchSubjects(anyLong())).thenReturn(Optional.of(new Teacher()));
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.of(new SchoolClass()));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminClassService.assignTeacherToSchoolClass(teacherRequest, CLASS_1A))
                .isInstanceOf(NoSuchSchoolSubjectException.class)
                .hasMessage("Such a school subject does not exist: " + teacherRequest.getTaughtSubject());
        verify(teacherInClassService, never()).assignTeacherToClass(any(), any(), any());
    }

    @Test
    void should_throw_exception_when_trying_to_assign_teacher_to_school_class_but_teacher_doesnt_teach_given_subject() {
        Teacher teacherWithOutSubjects = createTeacherNoSubjectsTaught();
        AddOrRemoveTeacherInClassDto teacherRequest = new AddOrRemoveTeacherInClassDto(ID_1, SUBJECT_BIOLOGY);
        when(teacherRepository.findByIdAndFetchSubjects(anyLong())).thenReturn(Optional.of(teacherWithOutSubjects));
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.of(new SchoolClass()));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(createSchoolSubject()));

        assertThatThrownBy(() -> adminClassService.assignTeacherToSchoolClass(teacherRequest, CLASS_1A))
                .isInstanceOf(TeacherDoesNotTeachSubjectException.class)
                .hasMessage("Adam Nowak does not teach " + teacherRequest.getTaughtSubject());
        verify(teacherInClassService, never()).assignTeacherToClass(any(), any(), any());
    }

    @Test
    void should_throw_exception_when_trying_to_assign_teacher_to_school_class_but_the_class_already_has_teacher_of_subject() {
        Teacher teacherWithOutSubjects = createTeacherOfBiology();
        SchoolClass schoolClassWithTeacher = createSchoolClassWithTeacher();
        AddOrRemoveTeacherInClassDto teacherRequest = new AddOrRemoveTeacherInClassDto(ID_1, SUBJECT_BIOLOGY);
        when(teacherRepository.findByIdAndFetchSubjects(anyLong())).thenReturn(Optional.of(teacherWithOutSubjects));
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.of(schoolClassWithTeacher));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(createSchoolSubject()));

        assertThatThrownBy(() -> adminClassService.assignTeacherToSchoolClass(teacherRequest, CLASS_1A))
                .isInstanceOf(ClassAlreadyHasTeacherException.class)
                .hasMessage("Alicja Kowalczyk already teaches biology in 1a");
        verify(teacherInClassService, never()).assignTeacherToClass(any(), any(), any());
    }

    @Test
    void should_throw_exception_when_school_class_doesnt_exist_while_trying_to_get_all_students_from_class() {
        when(schoolClassRepository.existsById(anyString())).thenReturn(false);

        assertThatThrownBy(() -> adminClassService.getAllStudentsInClass(CLASS_1A))
                .isInstanceOf(NoSuchSchoolClassException.class)
                .hasMessage("Such a school class does not exist: 1a");
    }

    @Test
    void should_correctly_delete_school_class() {
        when(schoolClassRepository.existsById(anyString())).thenReturn(true);

        adminClassService.deleteSchoolClass(CLASS_1A);

        verify(schoolClassRepository, timeout(1)).deleteTaughtClasses(any());
        verify(schoolClassRepository, timeout(1)).deleteById(any());
    }

    @Test
    void should_throw_exception_when_trying_to_remove_not_existing_school_class() {
        when(schoolClassRepository.existsById(anyString())).thenReturn(false);

        assertThatThrownBy(() -> adminClassService.deleteSchoolClass(CLASS_1A))
                .isInstanceOf(NoSuchSchoolClassException.class)
                .hasMessage("Such a school class does not exist: 1a");
    }


}