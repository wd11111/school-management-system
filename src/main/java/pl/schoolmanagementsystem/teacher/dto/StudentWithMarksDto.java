package pl.schoolmanagementsystem.teacher.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class StudentWithMarksDto {

    private Long studentId;
    private String name;
    private String surname;
    private List<BigDecimal> marks;
}
