package pl.schoolmanagementsystem.admin.schoolClass.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.model.Teacher;
import pl.schoolmanagementsystem.model.TeacherInClass;
import pl.schoolmanagementsystem.repository.TeacherInClassRepository;
import pl.schoolmanagementsystem.service.AdminTeacherInClassService;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminTeacherInClassServiceTest implements Samples {

    @Mock
    private TeacherInClassRepository teacherInClassRepository;

    @InjectMocks
    private AdminTeacherInClassService adminTeacherInClassService;

    @Test
    void should_add_teacher_to_class_when_teacher_in_class_already_exists() {
        Teacher teacher = createTeacherNoSubjectsTaught();
        SchoolClass schoolClass = createSchoolClass();
        TeacherInClass teacherInClass = new TeacherInClass();
        teacherInClass.setTaughtClasses(new HashSet<>());
        teacherInClass.setTaughtSubject(SUBJECT_BIOLOGY);
        when(teacherInClassRepository.findByTeacherAndTaughtSubject(any(), any())).thenReturn(Optional.of(teacherInClass));

        adminTeacherInClassService.addTeacherToClass(teacher, SUBJECT_BIOLOGY, schoolClass);

        assertThat(teacherInClass.getTaughtClasses()).hasSize(1);
        verify(teacherInClassRepository, times(1)).save(any());
    }
}