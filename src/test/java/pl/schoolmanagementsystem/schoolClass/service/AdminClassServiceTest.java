package pl.schoolmanagementsystem.schoolClass.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.exception.ClassAlreadyExistsException;
import pl.schoolmanagementsystem.common.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.common.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.common.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.repository.StudentRepository;
import pl.schoolmanagementsystem.schoolClass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.schoolClass.utils.SchoolClassMapper;
import pl.schoolmanagementsystem.schoolClass.utils.SchoolClassMapperStub;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminClassServiceTest implements Samples {

    public static final String CLASS_NAME = "1A";
    @Mock
    private SchoolClassRepository schoolClassRepository;

    @Mock
    private SchoolSubjectRepository schoolSubjectRepository;

    @Mock
    private StudentRepository studentRepository;

    @Spy
    private SchoolClassMapper schoolClassMapper = new SchoolClassMapperStub();

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
    void should_throw_exception_when_school_class_doesnt_exist_while_trying_to_get_all_students_from_class() {
        when(schoolClassRepository.existsById(anyString())).thenReturn(false);

        assertThatThrownBy(() -> adminClassService.getAllStudentsInClass(CLASS_1A))
                .isInstanceOf(NoSuchSchoolClassException.class)
                .hasMessage("Such a school class does not exist: 1A");
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
                .hasMessage("Such a school class does not exist: 1A");
    }

}
