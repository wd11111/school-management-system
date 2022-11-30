package pl.schoolmanagementsystem.common.teacher.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherOutputDto {

    private long id;
    private String name;
    private String surname;
    private Set<String> taughtSubjects;

}
