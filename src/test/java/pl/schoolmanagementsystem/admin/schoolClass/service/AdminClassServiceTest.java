package pl.schoolmanagementsystem.admin.schoolClass.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClassRepository;
import pl.schoolmanagementsystem.common.schoolClass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.common.schoolClass.exception.ClassAlreadyExistsException;
import pl.schoolmanagementsystem.common.schoolClass.exception.ClassAlreadyHasTeacherException;
import pl.schoolmanagementsystem.common.schoolClass.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.schoolSubject.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.common.student.StudentRepository;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.common.teacher.TeacherInClass;
import pl.schoolmanagementsystem.common.teacher.TeacherRepository;
import pl.schoolmanagementsystem.common.teacher.exception.NoSuchTeacherException;
import pl.schoolmanagementsystem.common.teacher.exception.TeacherDoesNotTeachSubjectException;
import pl.schoolmanagementsystem.schoolClass.dto.AddTeacherToClassDto;
import pl.schoolmanagementsystem.schoolClass.dto.TeacherInClassDto;
import pl.schoolmanagementsystem.schoolClass.service.AdminClassService;
import pl.schoolmanagementsystem.schoolClass.service.AdminTeacherInClassService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminClassServiceTest implements Samples {

    public static final String CLASS_NAME = "1a";
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

    @InjectMocks
    private AdminClassService adminClassService;

    @Test
    void should_create_school_class() {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName(CLASS_NAME);
        when(schoolClassRepository.existsById(anyString())).thenReturn(false);
        when(schoolClassRepository.save(any())).thenReturn(schoolClass);
        SchoolClassDto schoolClassDto = new SchoolClassDto(CLASS_NAME);

        SchoolClass result = adminClassService.createSchoolClass(schoolClassDto);

        assertThat(result).isSameAs(schoolClass);
    }

    @Test
    void should_throw_exception_when_school_class_already_exists() {
        SchoolClassDto schoolClassDto = new SchoolClassDto(CLASS_NAME);
        when(schoolClassRepository.existsById(anyString())).thenReturn(true);

        assertThatThrownBy(() -> adminClassService.createSchoolClass(schoolClassDto))
                .isInstanceOf(ClassAlreadyExistsException.class)
                .hasMessage("Class 1a already exists");
        verify(schoolClassRepository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_school_class_doesnt_exist() {
        when(schoolClassRepository.existsById(anyString())).thenReturn(false);

        assertThatThrownBy(() -> adminClassService.getTaughtSubjectsInClass(CLASS_NAME))
                .isInstanceOf(NoSuchSchoolClassException.class)
                .hasMessage("Such a school class does not exist: 1a");
    }

    @Test
    void should_add_teacher_to_school_class() {
        Teacher teacher = createTeacherOfBiology();
        TeacherInClass teacherInClass = new TeacherInClass();
        teacherInClass.setTeacher(teacher);
        AddTeacherToClassDto teacherRequest = new AddTeacherToClassDto(ID_1, SUBJECT_BIOLOGY);
        SchoolClass schoolClass = createSchoolClass();
        SchoolSubject schoolSubject = createSchoolSubject();
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.ofNullable(teacher));
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.ofNullable(schoolClass));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.ofNullable(schoolSubject));
        when(teacherInClassService.addTeacherToClass(any(), any(), any())).thenReturn(teacherInClass);

        TeacherInClassDto result = adminClassService.addTeacherToSchoolClass(teacherRequest, CLASS_1A);

        assertThat(result.getTeacherId()).isSameAs(teacherInClass.getTeacher().getId());
    }

    @Test
    void should_throw_exception_when_teacher_doesnt_exist() {
        AddTeacherToClassDto teacherRequest = new AddTeacherToClassDto(ID_1, SUBJECT_BIOLOGY);
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminClassService.addTeacherToSchoolClass(teacherRequest, CLASS_1A))
                .isInstanceOf(NoSuchTeacherException.class)
                .hasMessage("Teacher with such an id does not exist: " + teacherRequest.getTeacherId());
        verify(teacherInClassService, never()).addTeacherToClass(any(), any(), any());
    }

    @Test
    void should_throw_exception_when_school_class_doesnt_exist_while_adding_teacher() {
        AddTeacherToClassDto teacherRequest = new AddTeacherToClassDto(ID_1, SUBJECT_BIOLOGY);
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(new Teacher()));
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminClassService.addTeacherToSchoolClass(teacherRequest, CLASS_1A))
                .isInstanceOf(NoSuchSchoolClassException.class)
                .hasMessage("Such a school class does not exist: " + CLASS_1A);
        verify(teacherInClassService, never()).addTeacherToClass(any(), any(), any());
    }

    @Test
    void should_throw_exception_when_school_subject_doesnt_exist_while_adding_teacher() {
        AddTeacherToClassDto teacherRequest = new AddTeacherToClassDto(ID_1, SUBJECT_BIOLOGY);
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(new Teacher()));
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.of(new SchoolClass()));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminClassService.addTeacherToSchoolClass(teacherRequest, CLASS_1A))
                .isInstanceOf(NoSuchSchoolSubjectException.class)
                .hasMessage("Such a school subject does not exist: " + teacherRequest.getTaughtSubject());
        verify(teacherInClassService, never()).addTeacherToClass(any(), any(), any());
    }

    @Test
    void should_throw_exception_when_teacher_doesnt_teach_subject() {
        Teacher teacherWithOutSubjects = createTeacherNoSubjectsTaught();
        AddTeacherToClassDto teacherRequest = new AddTeacherToClassDto(ID_1, SUBJECT_BIOLOGY);
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacherWithOutSubjects));
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.of(new SchoolClass()));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(createSchoolSubject()));

        assertThatThrownBy(() -> adminClassService.addTeacherToSchoolClass(teacherRequest, CLASS_1A))
                .isInstanceOf(TeacherDoesNotTeachSubjectException.class)
                .hasMessage("Adam Nowak does not teach " + teacherRequest.getTaughtSubject());
        verify(teacherInClassService, never()).addTeacherToClass(any(), any(), any());
    }

    @Test
    void should_throw_exception_when_school_class_already_has_teacher_of_subject() {
        Teacher teacherWithOutSubjects = createTeacherOfBiology();
        SchoolClass schoolClassWithTeacher = createSchoolClassWithTeacher();
        AddTeacherToClassDto teacherRequest = new AddTeacherToClassDto(ID_1, SUBJECT_BIOLOGY);
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacherWithOutSubjects));
        when(schoolClassRepository.findById(anyString())).thenReturn(Optional.of(schoolClassWithTeacher));
        when(schoolSubjectRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(createSchoolSubject()));

        assertThatThrownBy(() -> adminClassService.addTeacherToSchoolClass(teacherRequest, CLASS_1A))
                .isInstanceOf(ClassAlreadyHasTeacherException.class)
                .hasMessage("Alicja Kowalczyk already teaches Biology in 1a");
        verify(teacherInClassService, never()).addTeacherToClass(any(), any(), any());
    }

    @Test
    void should_throw_exception_when_school_class_doesnt_exist_while_while_getting_students() {
        when(schoolClassRepository.existsById(anyString())).thenReturn(false);

        assertThatThrownBy(() -> adminClassService.getAllStudentsInClass(CLASS_1A))
                .isInstanceOf(NoSuchSchoolClassException.class)
                .hasMessage("Such a school class does not exist: 1a");
    }

    @Test
    void should_delete_school_class() {
        when(schoolClassRepository.existsById(anyString())).thenReturn(true);

        adminClassService.deleteSchoolClass(CLASS_1A);

        verify(schoolClassRepository, timeout(1)).deleteTaughtClasses(any());
        verify(schoolClassRepository, timeout(1)).deleteById(any());
    }

    @Test
    void should_throw_exception_when_school_class_doesnt_exist_while_while_deleting() {
        when(schoolClassRepository.existsById(anyString())).thenReturn(false);

        assertThatThrownBy(() -> adminClassService.deleteSchoolClass(CLASS_1A))
                .isInstanceOf(NoSuchSchoolClassException.class)
                .hasMessage("Such a school class does not exist: 1a");
    }


}