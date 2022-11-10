package pl.schoolmanagementsystem.teacherinclass.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherInClassOutputDto {

    private int teacherId;
    private String taughtSubject;
    private String schoolClassName;
}