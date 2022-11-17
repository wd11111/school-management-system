package pl.schoolmanagementsystem.common.teacher.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class TeacherInputDto {

    private String email;
    private String name;
    private String surname;
    private boolean isAdmin;
    private Set<String> taughtSubjects;
}
