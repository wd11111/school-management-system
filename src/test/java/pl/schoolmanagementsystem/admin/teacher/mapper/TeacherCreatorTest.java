package pl.schoolmanagementsystem.admin.teacher.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.admin.teacher.dto.CreateTeacherDto;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.common.user.Role;
import pl.schoolmanagementsystem.common.user.RoleRepository;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherCreatorTest implements Samples {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private TeacherCreator teacherCreator;

    @Test
    void should_create_teacher_with_one_role() {
        CreateTeacherDto createTeacherDto = CreateTeacherDto.builder().email(SURNAME).build();
        Set<SchoolSubject> subjectSet = Set.of(createSchoolSubject());
        when(roleRepository.findById(anyString())).thenReturn(Optional.of(new Role()));

        Teacher teacher = teacherCreator.createTeacher(createTeacherDto, subjectSet);

        assertThat(teacher.getAppUser().getRoles()).hasSize(1);
    }

    @Test
    void should_create_teacher_with_two_roles() {
        CreateTeacherDto createTeacherDto = CreateTeacherDto.builder().email(SURNAME).isAdmin(true).build();
        Set<SchoolSubject> subjectSet = Set.of(createSchoolSubject());
        when(roleRepository.findById(anyString())).thenReturn(Optional.of(new Role()));
        when(roleRepository.findById(anyString())).thenReturn(Optional.of(new Role()));

        Teacher teacher = teacherCreator.createTeacher(createTeacherDto, subjectSet);

        assertThat(teacher.getAppUser().getRoles()).hasSize(2);
    }
}