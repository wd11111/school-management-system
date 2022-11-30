package pl.schoolmanagementsystem.common.mark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MarkOutputDto {

    private long markId;
    private byte mark;
    private long studentId;
    private String subjectName;
}
