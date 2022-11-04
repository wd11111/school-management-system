package pl.schoolmanagementsystem.model.dto.output;

import lombok.*;
import pl.schoolmanagementsystem.model.dto.SchoolSubjectDto;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherOutputDto {

    private int id;
    private String name;
    private String surname;
    private Set<SchoolSubjectDto> taughtSubjects;

}
