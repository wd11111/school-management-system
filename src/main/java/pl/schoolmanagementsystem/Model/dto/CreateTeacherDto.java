package pl.schoolmanagementsystem.Model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeacherDto {

    private String name;

    private String surname;

    private Set<String> taughtSubjects = new HashSet<>();
}
