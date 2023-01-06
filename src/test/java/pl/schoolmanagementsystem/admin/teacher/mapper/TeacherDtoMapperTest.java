package pl.schoolmanagementsystem.admin.teacher.mapper;

import org.junit.jupiter.api.Test;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.admin.teacher.dto.TeacherDto;
import pl.schoolmanagementsystem.common.teacher.Teacher;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherDtoMapperTest implements Samples {

    @Test
    void should_map_teacher_to_teacher_response_dto() {
        Teacher teacher = createTeacherOfBiology();
        TeacherDto expected =  createTeacherResponseDto();

        TeacherDto result = TeacherDtoMapper.mapToTeacherResponseDto(teacher);

        assertThat(result.getId()).isEqualTo(expected.getId());
        assertThat(result.getTaughtSubjects()).isEqualTo(expected.getTaughtSubjects());
        assertThat(result.getName()).isEqualTo(expected.getName());
        assertThat(result.getSurname()).isEqualTo(expected.getSurname());
    }

}