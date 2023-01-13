package pl.schoolmanagementsystem.common.role;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.common.model.Teacher;
import pl.schoolmanagementsystem.common.repository.RoleRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleAdderTest implements Samples {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleAdder roleAdder;

    @Test
    void should_add_roles_to_student() {
        when(roleRepository.findById(RoleAdder.ROLE_STUDENT)).thenReturn(Optional.of(createRole()));
        Student student = createStudent2();

        roleAdder.addRoles(student);

        assertThat(student.getAppUser().getRoles()).hasSize(1);
    }

    @Test
    void should_throw_exception_when_failed_to_add_role_for_student() {
        when(roleRepository.findById(RoleAdder.ROLE_STUDENT)).thenReturn(Optional.empty());
        Student student = createStudent2();

        assertThatThrownBy(() -> roleAdder.addRoles(student))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Failed to add role");
    }

    @Test
    void should_add_one_role_to_teacher() {
        when(roleRepository.findById(RoleAdder.ROLE_TEACHER)).thenReturn(Optional.of(createRole()));
        Teacher teacher = createTeacherNoSubjectsTaught();

        roleAdder.addRoles(teacher, IS_NOT_ADMIN);

        assertThat(teacher.getAppUser().getRoles()).hasSize(1);
    }

    @Test
    void should_add_two_roles_to_teacher() {
        when(roleRepository.findById(RoleAdder.ROLE_TEACHER)).thenReturn(Optional.of(createRole()));
        when(roleRepository.findById(RoleAdder.ROLE_ADMIN)).thenReturn(Optional.of(createRole()));
        Teacher teacher = createTeacherNoSubjectsTaught();

        roleAdder.addRoles(teacher, IS_ADMIN);

        assertThat(teacher.getAppUser().getRoles()).hasSize(2);
    }

    @Test
    void should_throw_exception_when_role_teacher_not_found() {
        when(roleRepository.findById(RoleAdder.ROLE_TEACHER)).thenReturn(Optional.empty());
        Teacher teacher = createTeacherNoSubjectsTaught();

        assertThatThrownBy(() -> roleAdder.addRoles(teacher, IS_NOT_ADMIN))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Failed to add role");
    }

    @Test
    void should_throw_exception_when_role_admin_not_found() {
        when(roleRepository.findById(RoleAdder.ROLE_TEACHER)).thenReturn(Optional.of(createRole()));
        when(roleRepository.findById(RoleAdder.ROLE_ADMIN)).thenReturn(Optional.empty());
        Teacher teacher = createTeacherNoSubjectsTaught();

        assertThatThrownBy(() -> roleAdder.addRoles(teacher, IS_ADMIN))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Failed to add role");
    }

}