package pl.schoolmanagementsystem.Model.dto;

import lombok.*;
import pl.schoolmanagementsystem.Model.Mark;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarkAvg {

    private String subject;
    private List<Mark> marks;
}
