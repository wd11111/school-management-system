package pl.schoolmanagementsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.schoolmanagementsystem.model.SchoolSubject;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarkAvgDto {

    private String subject;
    private double avg;
}
