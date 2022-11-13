package pl.schoolmanagementsystem.teacher.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherInputDto {

    private String email;
    private String name;
    private String surname;
    private String password;
    private boolean isAdmin;
    private Set<String> taughtSubjects;
}
