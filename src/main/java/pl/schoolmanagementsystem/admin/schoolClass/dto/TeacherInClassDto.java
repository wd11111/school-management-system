package pl.schoolmanagementsystem.admin.schoolClass.dto;

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

    private long teacherId;
    private String taughtSubject;
    private Set<String> schoolClassName;
}