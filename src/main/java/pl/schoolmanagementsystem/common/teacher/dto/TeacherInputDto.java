package pl.schoolmanagementsystem.common.teacher.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherInputDto {

    private String email;
    private String name;
    private String surname;
    private String password;
    private boolean isAdmin;
    private Set<String> taughtSubjects;
}
