package pl.schoolmanagementsystem.mark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MarkOutputDto {

    private int markId;
    private int mark;
    private int StudentId;
}
