package pl.schoolmanagementsystem.admin.student.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.mapper.StudentCreator;
import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.model.Student;
import pl.schoolmanagementsystem.model.dto.StudentRequestDto;
import pl.schoolmanagementsystem.model.Role;
import pl.schoolmanagementsystem.repository.RoleRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentCreatorTest implements Samples {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private StudentCreator studentCreator;

    @Test
    void should_create_student_with_one_role() {
        StudentRequestDto studentRequestDto = new StudentRequestDto();
        SchoolClass schoolClass = createSchoolClass();
        when(roleRepository.findById(anyString())).thenReturn(Optional.of(new Role()));

        Student student = studentCreator.createStudent(studentRequestDto, schoolClass);

        assertThat(student.getAppUser().getRoles()).hasSize(1);
    }

}