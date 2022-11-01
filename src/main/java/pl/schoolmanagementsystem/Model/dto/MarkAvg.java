package pl.schoolmanagementsystem.Model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.schoolmanagementsystem.Model.Mark;

import java.util.List;

@AllArgsConstructor
@Data
public class MarkAvg {

    private String subject;
    private List<Mark> marks;
}
