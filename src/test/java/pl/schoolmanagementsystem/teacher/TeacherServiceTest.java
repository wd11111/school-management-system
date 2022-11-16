/*
package pl.schoolmanagementsystem.teacher;

import com.sun.security.auth.UserPrincipal;
import org.junit.jupiter.api.Test;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.common.teacher.TeacherRepository;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto;
import pl.schoolmanagementsystem.teacher.exception.NoSuchTeacherEmailException;
import pl.schoolmanagementsystem.teacher.exception.NoSuchTeacherException;
import pl.schoolmanagementsystem.teacher.exception.TeacherAlreadyTeachesSubjectException;
import pl.schoolmanagementsystem.teacher.exception.TeacherDoesNotTeachSubjectException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class TeacherServiceTest implements TeacherSamples {

    private final TeacherRepository teacherRepository = mock(TeacherRepository.class);

    private final TeacherService teacherService = new TeacherService(teacherRepository);

    @Test
    void should_correctly_save_teacher() {
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
    void should_delete_teacher() {
        teacherService.deleteById(ID_2);

        verify(teacherRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void should_correctly_return_teacher_by_id() {
        Teacher teacher = savedTeacher2();
        when(teacherRepository.findById(anyInt())).thenReturn(Optional.ofNullable(teacher));

        Teacher result = teacherService.findByIdOrThrow(ID_2);

        assertThat(result).isEqualTo(teacher);
    }

    @Test
    void should_throw_exception_when_teacher_not_found_by_id() {
        when(teacherRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teacherService.findByIdOrThrow(ID_2))
                .isInstanceOf(NoSuchTeacherException.class)
                .hasMessageContaining(String.format("Teacher with such an id does not exist: %d", ID_2));
    }

    @Test
    void should_return_taught_classes_by_teacher() {
        List<SubjectAndClassOutputDto> expectedList = listOfTaughtClasses();
        when(teacherRepository.findTaughtClassesByTeacher(anyInt())).thenReturn(expectedList);

        List<SubjectAndClassOutputDto> result = teacherService.findTaughtClassesByTeacher(ID_1);

        assertThat(result).isEqualTo(expectedList);
    }

    @Test
    void should_return_all_teachers() {
        List<Teacher> expectedList = listOfTeachers();
        when(teacherRepository.findAll()).thenReturn(expectedList);

        List<Teacher> result = teacherService.findAllTeachersInSchool();

        assertThat(result).isEqualTo(expectedList);
    }

    @Test
    void should_correctly_return_teacher_by_email() {
        Teacher teacher = savedTeacher1();
        when(teacherRepository.findByEmail_Email(anyString())).thenReturn(Optional.ofNullable(teacher));

        Teacher result = teacherService.findByEmailOrThrow(ADAM);

        assertThat(result).isEqualTo(teacher);
    }

    @Test
    void should_throw_exception_when_teacher_not_found_by_email() {
        when(teacherRepository.findByEmail_Email(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teacherService.findByEmailOrThrow(ADAM))
                .isInstanceOf(NoSuchTeacherEmailException.class)
                .hasMessageContaining(String.format("Teacher with such an email does not exist: %s", ADAM));
    }

    @Test
    void should_check_if_teacher_teaches_subject() {
        Teacher teacher = teacher2();
        SchoolSubject schoolSubject = schoolSubject();

        teacherService.makeSureTeacherTeachesThisSubject(teacher, schoolSubject);

    }

    @Test
    void should_throw_exception_when_teacher_doesnt_teach_subject() {
        Teacher teacher = teacher1();
        SchoolSubject schoolSubject = schoolSubject();

        assertThatThrownBy(() -> teacherService.makeSureTeacherTeachesThisSubject(teacher, schoolSubject))
                .isInstanceOf(TeacherDoesNotTeachSubjectException.class)
                .hasMessageContaining(String.format("%s %s does not teach %s",
                        teacher.getName(), teacher.getSurname(), schoolSubject.getName()));
    }

    @Test
    void should_check_if_teacher_doesnt_already_teach_subject() {
        Teacher teacher = teacher1();
        SchoolSubject schoolSubject = schoolSubject();

        teacherService.checkIfTeacherDoesntAlreadyTeachThisSubject(teacher, schoolSubject);
    }

    @Test
    void should_throw_exception_when_teacher_already_teaches_subject() {
        Teacher teacher = teacher2();
        SchoolSubject schoolSubject = schoolSubject();

        assertThatThrownBy(() -> teacherService.checkIfTeacherDoesntAlreadyTeachThisSubject(teacher, schoolSubject))
                .isInstanceOf(TeacherAlreadyTeachesSubjectException.class)
                .hasMessageContaining(String.format("%s %s already teaches %s",
                        teacher.getName(), teacher.getSurname(), schoolSubject.getName()));
    }

    @Test
    void should_correctly_check_if_teacher_exists() {
        when(teacherRepository.existsById(anyInt())).thenReturn(true);

        teacherService.makeSureTeacherExists(ID_1);

        verify(teacherRepository, times(1)).existsById(anyInt());
    }

    @Test
    void should_throw_exception_when_teacher_doesnt_exist() {
        when(teacherRepository.existsById(anyInt())).thenReturn(false);

        assertThatThrownBy(() -> teacherService.makeSureTeacherExists(ID_1))
                .isInstanceOf(NoSuchTeacherException.class)
                .hasMessageContaining(String.format("Teacher with such an id does not exist: %d", ID_1));

        verify(teacherRepository, times(1)).existsById(anyInt());
    }

    @Test
    void should_return_id_from_principals() {
        Principal principal = new UserPrincipal(ADAM);
        int expectedId = ID_1;
        when(teacherRepository.findIdByEmail(anyString())).thenReturn(expectedId);

        int result = teacherService.getIdFromPrincipals(principal);

        assertThat(result).isEqualTo(expectedId);
    }
}
*/
