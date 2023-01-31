package pl.schoolmanagementsystem.teacher.utils;

import pl.schoolmanagementsystem.common.model.AppUser;
import pl.schoolmanagementsystem.common.model.SchoolSubject;
import pl.schoolmanagementsystem.common.model.Teacher;
import pl.schoolmanagementsystem.teacher.dto.CreateTeacherDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherDto;

import java.util.List;
import java.util.Set;

public class TeacherMapperStub implements TeacherMapper {

    @Override
    public Teacher mapCreateDtoToEntity(CreateTeacherDto createTeacherDto, Set<SchoolSubject> taughtSubjects, AppUser appUser) {
        return Teacher.builder()
                .name(createTeacherDto.getName())
                .surname(createTeacherDto.getSurname())
                .taughtSubjects(taughtSubjects)
                .appUser(appUser)
                .build();
    }

    @Override
    public TeacherDto mapEntityToDto(Teacher teacher) {
        return TeacherDto.builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .surname(teacher.getSurname())
                .build();
    }

    @Override
    public String schoolSubjectToString(SchoolSubject schoolSubject) {
        return TeacherMapper.super.schoolSubjectToString(schoolSubject);
    }

    @Override
    public List<TeacherDto> mapEntitiesToDtos(List<Teacher> teachers) {
        return teachers.stream()
                .map(this::mapEntityToDto)
                .toList();
    }
}
