package pl.schoolmanagementsystem.schoolClass.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TeacherInClassDto {

    private Long teacherId;
    private String taughtSubject;
    private Set<String> taughtClasses;
}