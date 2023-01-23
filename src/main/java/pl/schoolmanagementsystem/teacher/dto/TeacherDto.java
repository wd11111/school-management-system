package pl.schoolmanagementsystem.teacher.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDto {

    private Long id;
    private String name;
    private String surname;
    private Set<String> taughtSubjects;

}
