package pl.schoolmanagementsystem.model.dto.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
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
