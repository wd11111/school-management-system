package pl.schoolmanagementsystem.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MarkDto {

    private int teacherId;
    private int studentId;
    private int mark;
    private String subject;

}