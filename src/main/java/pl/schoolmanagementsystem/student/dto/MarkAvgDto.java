package pl.schoolmanagementsystem.student.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MarkAvgDto {

    private String subject;
    private Double avg;
}
