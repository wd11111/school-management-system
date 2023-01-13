package pl.schoolmanagementsystem.schoolSubject.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.common.exception.SubjectAlreadyExistsException;
import pl.schoolmanagementsystem.common.repository.SchoolSubjectRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminSubjectServiceTest implements Samples {

    @Mock
    private SchoolSubjectRepository schoolSubjectRepository;

    @InjectMocks
    private AdminSubjectService adminSubjectService;

    @Test
    void should_create_subject() {
        SchoolSubjectDto schoolSubjectDto = new SchoolSubjectDto(SUBJECT_HISTORY);

        adminSubjectService.createSchoolSubject(schoolSubjectDto);

        verify(schoolSubjectRepository, times(1)).save(any());
    }

    @Test
    void should_throw_exception_when_subject_already_exists() {
        SchoolSubjectDto schoolSubjectDto = new SchoolSubjectDto(SUBJECT_HISTORY);
        when(schoolSubjectRepository.existsById(anyString())).thenReturn(true);

        assertThatThrownBy(() -> adminSubjectService.createSchoolSubject(schoolSubjectDto))
                .isInstanceOf(SubjectAlreadyExistsException.class)
                .hasMessage("Subject History already exists");
        verify(schoolSubjectRepository, never()).save(any());
    }

    @Test
    void should_delete_subject() {
        when(schoolSubjectRepository.existsById(anyString())).thenReturn(true);

        adminSubjectService.deleteSchoolSubject(SUBJECT_BIOLOGY);

        verify(schoolSubjectRepository, times(1)).deleteTaughtSubjects(any());
        verify(schoolSubjectRepository, times(1)).deleteById(any());
    }

    @Test
    void should_throw_exception_when_subject_doesnt_exist() {
        when(schoolSubjectRepository.existsById(anyString())).thenReturn(false);

        assertThatThrownBy(() -> adminSubjectService.deleteSchoolSubject(SUBJECT_BIOLOGY))
                .isInstanceOf(NoSuchSchoolSubjectException.class)
                .hasMessage("Such a school subject does not exist: Biology");

        verify(schoolSubjectRepository, never()).deleteTaughtSubjects(any());
        verify(schoolSubjectRepository, never()).deleteById(any());
    }
}