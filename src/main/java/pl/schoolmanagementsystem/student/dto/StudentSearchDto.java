package pl.schoolmanagementsystem.student.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class StudentSearchDto {

    private Long id;
    private String name;
    private String surname;
    private String schoolClass;
    private LocalDate birthDate;
}

