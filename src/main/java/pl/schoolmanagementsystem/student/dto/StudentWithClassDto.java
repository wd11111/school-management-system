package pl.schoolmanagementsystem.student.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentWithClassDto {

    private Long id;
    private String name;
    private String surname;
    private String schoolClass;
}
