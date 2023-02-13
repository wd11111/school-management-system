package pl.schoolmanagementsystem.schoolSubject.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.common.exception.SubjectAlreadyExistsException;
import pl.schoolmanagementsystem.common.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.schoolSubject.utils.SchoolSubjectMapper;
import pl.schoolmanagementsystem.schoolSubject.utlis.SchoolSubjectMapperStub;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminSubjectServiceTest implements Samples {

    @Mock
    private SchoolSubjectRepository schoolSubjectRepository;

    @Spy
    private SchoolSubjectMapper schoolSubjectMapper = new SchoolSubjectMapperStub();

    @InjectMocks
    private AdminSubjectService adminSubjectService;

    @Test
    void should_correctly_create_subject() {
        SchoolSubjectDto schoolSubjectDto = new SchoolSubjectDto(SUBJECT_HISTORY);

        adminSubjectService.createSchoolSubject(schoolSubjectDto);

        verify(schoolSubjectRepository, times(1)).save(any());
    }

    @Test
    void should_throw_exception_when_trying_to_create_subject_but_name_is_already_taken() {
        SchoolSubjectDto schoolSubjectDto = new SchoolSubjectDto(SUBJECT_HISTORY);
        when(schoolSubjectRepository.existsById(anyString())).thenReturn(true);

        assertThatThrownBy(() -> adminSubjectService.createSchoolSubject(schoolSubjectDto))
                .isInstanceOf(SubjectAlreadyExistsException.class)
                .hasMessage("Subject history already exists");
        verify(schoolSubjectRepository, never()).save(any());
    }

    @Test
    void should_correctly_delete_subject() {
        when(schoolSubjectRepository.existsById(anyString())).thenReturn(true);

        adminSubjectService.deleteSchoolSubject(SUBJECT_BIOLOGY);

        verify(schoolSubjectRepository, times(1)).deleteTaughtSubjects(any());
        verify(schoolSubjectRepository, times(1)).deleteById(any());
    }

    @Test
    void should_throw_exception_when_trying_to_delete_school_subject_but_it_doesnt_exist() {
        when(schoolSubjectRepository.existsById(anyString())).thenReturn(false);

        assertThatThrownBy(() -> adminSubjectService.deleteSchoolSubject(SUBJECT_BIOLOGY))
                .isInstanceOf(NoSuchSchoolSubjectException.class)
                .hasMessage("Such a school subject does not exist: biology");

        verify(schoolSubjectRepository, never()).deleteTaughtSubjects(any());
        verify(schoolSubjectRepository, never()).deleteById(any());
    }
}