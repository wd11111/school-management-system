package pl.schoolmanagementsystem.teacher.service;

import org.junit.jupiter.api.Test;
import pl.schoolmanagementsystem.teacher.Teacher;
import pl.schoolmanagementsystem.teacher.TeacherRepository;
import pl.schoolmanagementsystem.teacher.TeacherService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TeacherServiceTest implements TeacherSamples {

    private TeacherRepository teacherRepository = mock(TeacherRepository.class);

    private TeacherService teacherService = new TeacherService(teacherRepository);

    @Test
    void should_correctly_add_subject() {
        Teacher teacherToSave = teacher1();
        Teacher expectedTeacher = savedTeacher1();
        when(teacherRepository.save(teacherToSave)).thenReturn(expectedTeacher);

        Teacher result = teacherService.saveTeacher(teacherToSave);

        assertThat(result).isEqualTo(expectedTeacher);
    }

    @Test
    void should_correctly_add_subject_to_teacher() {
        Teacher teacher = savedTeacher1();
        then(teacher.getTaughtSubjects().size()).isZero();

        teacherService.addSubjectToTeacher(teacher, schoolSubject());

        assertThat(teacher.getTaughtSubjects().size()).isOne();
    }

    @Test
    void should_correctly_find_teacher() {
        Teacher teacher = savedTeacher2();
        when(teacherRepository.findById(anyInt())).thenReturn(Optional.ofNullable(teacher));

        Teacher result = teacherService.findByIdOrThrow(2);

        assertThat(result).isEqualTo(teacher);
    }
}
