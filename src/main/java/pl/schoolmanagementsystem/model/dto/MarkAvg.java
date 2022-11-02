package pl.schoolmanagementsystem.model.dto;

import lombok.*;
import pl.schoolmanagementsystem.model.Mark;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarkAvg {

    private String subject;
    private List<Mark> marks;
}
