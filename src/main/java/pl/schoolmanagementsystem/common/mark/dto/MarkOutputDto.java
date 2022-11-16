package pl.schoolmanagementsystem.common.mark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MarkOutputDto {

    private int markId;
    private int mark;
    private int studentId;
    private String subjectName;
}
