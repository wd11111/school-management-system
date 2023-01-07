package pl.schoolmanagementsystem.admin.teacher.mapper;

import org.junit.jupiter.api.Test;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.teacher.dto.TeacherDto;
import pl.schoolmanagementsystem.teacher.utils.TeacherMapper;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherMapperTest implements Samples {

    @Test
    void should_map_teacher_to_teacher_response_dto() {
        Teacher teacher = createTeacherOfBiology();
        TeacherDto expected =  createTeacherResponseDto();

        TeacherDto result = TeacherMapper.mapEntityToDto(teacher);

        assertThat(result.getId()).isEqualTo(expected.getId());
        assertThat(result.getTaughtSubjects()).isEqualTo(expected.getTaughtSubjects());
        assertThat(result.getName()).isEqualTo(expected.getName());
        assertThat(result.getSurname()).isEqualTo(expected.getSurname());
    }

}