package pl.schoolmanagementsystem.admin.teacher.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherResponseDto {

    private long id;
    private String name;
    private String surname;
    private Set<String> taughtSubjects;

}
