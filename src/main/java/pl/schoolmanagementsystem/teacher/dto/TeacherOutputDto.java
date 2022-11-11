package pl.schoolmanagementsystem.teacher.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.schoolmanagementsystem.schoolsubject.dto.SchoolSubjectDto;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherOutputDto {

    private int id;
    private String name;
    private String surname;
    private Set<SchoolSubjectDto> taughtSubjects;

}
