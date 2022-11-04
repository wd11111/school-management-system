package pl.schoolmanagementsystem.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.schoolmanagementsystem.model.SchoolSubject;
import pl.schoolmanagementsystem.model.Teacher;
import pl.schoolmanagementsystem.model.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.model.dto.output.TeacherOutputDto;
import pl.schoolmanagementsystem.repository.TeacherRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TeacherMapper {

    private final TeacherRepository teacherRepository;

    public TeacherOutputDto mapTeacherToTeacherDto(Teacher teacher) {
        return TeacherOutputDto.builder()
                .id(teacher.getTeacherId())
                .name(teacher.getName())
                .surname(teacher.getSurname())
                .taughtSubjects(mapListOfSubjectsToListOfSubjectDto(teacher.getTaughtSubjects()))
                .build();
    }

    public Set<SchoolSubjectDto> mapListOfSubjectsToListOfSubjectDto(Set<SchoolSubject> subjects) {
        return subjects.stream().map(subject -> new SchoolSubjectDto(subject.getSubjectName()))
                .collect(Collectors.toSet());
    }
}
