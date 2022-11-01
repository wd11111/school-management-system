package pl.schoolmanagementsystem.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class MarkAvg {

    private String subject;
    private List<Mark> marks;
}
