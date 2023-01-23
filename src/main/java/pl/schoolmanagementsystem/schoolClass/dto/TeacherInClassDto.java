package pl.schoolmanagementsystem.schoolClass.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TeacherInClassDto {

    private Long teacherId;
    private String taughtSubject;
    private Set<String> taughtClasses;
}