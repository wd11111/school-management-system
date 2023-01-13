package pl.schoolmanagementsystem.schoolClass.utils;

import org.junit.jupiter.api.Test;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.teacher.TeacherInClass;
import pl.schoolmanagementsystem.schoolClass.dto.TeacherInClassDto;

import static org.assertj.core.api.Assertions.assertThat;


class TeacherInClassMapperTest implements Samples {

    @Test
    void should_map_entity_to_dto() {
        TeacherInClass teacherInClass = createTeacherInClass();

        TeacherInClassDto result = TeacherInClassMapper.mapEntityToDto(teacherInClass);

        assertThat(result.getTeacherId()).isEqualTo(teacherInClass.getTeacher().getId());
        assertThat(result.getTaughtSubject()).isEqualTo(result.getTaughtSubject());
        assertThat(result.getTaughtClasses()).hasSize(1);
    }

}